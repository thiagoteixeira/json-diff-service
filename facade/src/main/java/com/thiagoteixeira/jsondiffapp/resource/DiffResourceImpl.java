package com.thiagoteixeira.jsondiffapp.resource;

import com.thiagoteixeira.jsondiffapp.dto.JsonDto;
import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.service.DiffService;
import com.thiagoteixeira.jsondiffapp.vo.DiffRequest;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@inheritDoc}
 * @see DiffResource
 * 
 * @author thiagoteixeira
 */
@RestController
public class DiffResourceImpl implements DiffResource {

    private final DiffService service;

    @Autowired
    public DiffResourceImpl(DiffService service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     * @see DiffResource#saveLeft(Long, DiffRequest) 
     */
    @Override
    public ResponseEntity<DataResponse> saveLeft(final Long id, final DiffRequest request) {
        var data = JsonDto
            .builder()
            .withId(id)
            .withSide(JsonSide.LEFT)
            .withValue(request.getValue())
            .build();

        Optional<DataResponse> response = this.service.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

    /**
     * {@inheritDoc}
     * @see DiffResource#saveRight(Long, DiffRequest)
     */
    @Override
    public ResponseEntity<DataResponse> saveRight(final Long id, final DiffRequest request) {
        var data = JsonDto
            .builder()
            .withId(id)
            .withSide(JsonSide.RIGHT)
            .withValue(request.getValue())
            .build();

        Optional<DataResponse> response = this.service.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

    /**
     * {@inheritDoc}
     * @see DiffResource#getDiff(Long)
     */
    @Override
    public ResponseEntity<DiffResponse> getDiff(final Long id) {
        Optional<DiffResponse> response = this.service.diff(id);
        return response.isPresent() ? ResponseEntity.ok(response.get()) : ResponseEntity.notFound().build();
    }
}
