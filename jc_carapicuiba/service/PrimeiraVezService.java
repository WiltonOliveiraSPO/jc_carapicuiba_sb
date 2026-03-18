package com.jc_carapicuiba.service;

import com.jc_carapicuiba.model.PrimeiraVez;
import com.jc_carapicuiba.repository.PrimeiraVezRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrimeiraVezService {

    private final PrimeiraVezRepository repository;

    public PrimeiraVezService(PrimeiraVezRepository repository) {
        this.repository = repository;
    }

    public List<PrimeiraVez> listar() {
        return repository.findAllByOrderByDataPrimVezDesc();
    }

    public PrimeiraVez salvar(PrimeiraVez primeiraVez) {
        return repository.save(primeiraVez);
    }

    public PrimeiraVez buscar(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    public List<PrimeiraVez> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCaseOrderByDataPrimVezDesc(nome);
    }

    public PrimeiraVez primeiro() {
        return repository.findFirstByOrderByCodPrimVezAsc().orElse(new PrimeiraVez());
    }

    public PrimeiraVez ultimo() {
        return repository.findFirstByOrderByCodPrimVezDesc().orElse(new PrimeiraVez());
    }

    public Optional<PrimeiraVez> anteriorOptional(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findFirstByCodPrimVezLessThanOrderByCodPrimVezDesc(id);
    }

    public Optional<PrimeiraVez> proximoOptional(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findFirstByCodPrimVezGreaterThanOrderByCodPrimVezAsc(id);
    }
}
