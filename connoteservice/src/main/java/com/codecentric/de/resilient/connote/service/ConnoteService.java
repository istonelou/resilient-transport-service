package com.codecentric.de.resilient.connote.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codecentric.de.resilient.dto.ConnoteDTO;
import com.codecentric.de.resilient.connote.entity.Connote;
import com.codecentric.de.resilient.connote.repository.ConnoteRepository;
import com.codecentric.de.resilient.connote.utils.RandomConnoteGenerator;

/**
 * @author Benjamin Wilms
 */
@Service
public class ConnoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnoteService.class);

    private ConnoteRepository connoteRepository;

    private final RandomConnoteGenerator connoteGenerator;

    @Autowired
    public ConnoteService(ConnoteRepository connoteRepository, RandomConnoteGenerator connoteGenerator) {

        this.connoteRepository = connoteRepository;
        this.connoteGenerator = connoteGenerator;
    }

    public ConnoteDTO createConnote() {

        Long nextConnote;

        do {
            nextConnote = connoteGenerator.randomNumber();

        } while (!isUnique(nextConnote));

        Connote connoteSaved = createConnote(nextConnote);

        ConnoteDTO connoteDTO = new ConnoteDTO();
        connoteDTO.setConnote(connoteSaved.getConnote());
        connoteDTO.setCreated(connoteSaved.getCreated().toDate());

        return connoteDTO;

    }

    private Connote createConnote(Long connote) {

        Connote connoteSaved = connoteRepository.save(new Connote(connote));

        LOGGER.debug(LOGGER.isDebugEnabled() ? "Saved new Connote entity: " + connoteSaved.getConnote() : null);

        return connoteSaved;
    }

    private boolean isUnique(Long connote) {

        return connoteRepository.findOne(connote) == null;

    }

}
