package br.edu.ifmt.clientes;

import java.net.URI;
import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientRepository repository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Page<Client> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int linesPerPage,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "name") String orderBy) {
        if (page < 0 || linesPerPage < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paginação inválida");
        }
        switch (orderBy) {
            case "id", "name", "cpf", "income", "birthDate", "children" -> {
                Sort sort = Sort.by(direction, orderBy).and(Sort.by("id"));
                return repository.findAll(PageRequest.of(page, linesPerPage, sort));
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ordenação inválida");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping
    public ResponseEntity<Client> insert(@RequestBody ClientData data) {
        Client client = repository.save(
                new Client(data.name(), data.cpf(), data.income(), data.birthDate(), data.children()));
        return ResponseEntity.created(URI.create("/clients/" + client.getId())).body(client);
    }

    @PutMapping("/{id}")
    @Transactional
    public Client update(@PathVariable Long id, @RequestBody ClientData data) {
        Client client = requireClient(id);
        client.update(data.name(), data.cpf(), data.income(), data.birthDate(), data.children());
        return client;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.delete(requireClient(id));
        return ResponseEntity.noContent().build();
    }

    private Client requireClient(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public record ClientData(String name, String cpf, Double income, Instant birthDate, Integer children) {
    }
}
