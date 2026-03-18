package com.jc_carapicuiba.service;

import com.jc_carapicuiba.model.Gratidao;
import com.jc_carapicuiba.repository.GratidaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GratidaoService {

    private final GratidaoRepository repository;

    public GratidaoService(GratidaoRepository repository) {
        this.repository = repository;
    }

    public List<Gratidao> listar() {
        return repository.findAllByOrderByDtGratidaoDesc();
    }

    public Gratidao salvar(Gratidao gratidao) {
        return repository.save(gratidao);
    }

    public Gratidao buscar(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    public List<Gratidao> buscarPorTipo(String tipo) {
        return repository.findByTipoGratidaoOrderByDtGratidaoDesc(tipo);
    }

    public Gratidao primeiro() {
        return repository.findFirstByOrderByCodGratidaoAsc().orElse(new Gratidao());
    }

    public Gratidao ultimo() {
        return repository.findFirstByOrderByCodGratidaoDesc().orElse(new Gratidao());
    }

    public Optional<Gratidao> anteriorOptional(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findFirstByCodGratidaoLessThanOrderByCodGratidaoDesc(id);
    }

    public Optional<Gratidao> proximoOptional(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findFirstByCodGratidaoGreaterThanOrderByCodGratidaoAsc(id);
    }
}
