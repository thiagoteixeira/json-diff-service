package com.thiagoteixeira.jsondiffbusiness.service;

import com.thiagoteixeira.jsondiffbusiness.remote.client.JsonDataClient;
import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import com.thiagoteixeira.jsondiffbusiness.vo.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 * @see DiffService
 *
 * @author thiagoteixeira
 */
@Service
public class DiffServiceImpl implements DiffService {

  private static final Logger logger = LoggerFactory.getLogger(DiffServiceImpl.class);

  private final JsonDataClient client;

  @Autowired
  public DiffServiceImpl(final JsonDataClient client) {
    this.client = client;
  }

  @Override
  public Optional<Result> compareJsonSides(long id) {
    logger.info("Comparing JSON sides");
    final var response = this.client.find(id);
    if(response.isPresent()){
      final var data = response.get();
      logger.info("Comparing JSON '{}' sides left: '{}' and right: '{}'", id, data.getLeft(), data.getRight());
      var result = compare(data);
      return Optional.ofNullable(result);
    }
    logger.info("JSON '{}' no found", id);
    return Optional.empty();
  }

  /**
   * Compares the bytes of the sides of a {@link JsonDataResponse} object instance.
   * @param data The {@link JsonDataResponse} object instance that will have its sides compared.
   * @return An {@link Result} object instance with a message regarding the result of the comparison. If sides are equal
   *         then {@link Result.Type#EQUAL_CONTENT}, otherwise check if the sizes are not equal and then the message is
   *         {@link Result.Type#NOT_EQUAL_SIZE}, already if they have the same size but different content then the
   *         message will be represented by {@link Result.Type#EQUAL_SIZE_DIFFERENT_CONTENT} with the offsets.
   */
  private Result compare(final JsonDataResponse data) {
    final var left = data.getLeft();
    final var right = data.getRight();

    final byte[] leftBytes = left != null ? left.getBytes() : Strings.EMPTY.getBytes();
    final byte[] rightBytes = right != null ? right.getBytes() : Strings.EMPTY.getBytes();

    final boolean result = Arrays.equals(leftBytes, rightBytes);

    if(result){
      return new Result(Result.Type.EQUAL_CONTENT);
    }

    if(leftBytes.length != rightBytes.length){
      return new Result(Result.Type.NOT_EQUAL_SIZE);
    } else {
      final List<String> offsets = new ArrayList<>();
      var tmp = 0;
      for (int index = 0; index < leftBytes.length; index++) {
        tmp = leftBytes[index] ^ rightBytes[index];
        if(tmp != 0){
          offsets.add(String.valueOf(index));
        }
      }
      final var indexes = String.join(", ", offsets);
      return new Result(Result.Type.EQUAL_SIZE_DIFFERENT_CONTENT, indexes);
    }
  }
}
