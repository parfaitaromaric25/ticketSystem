package com.example.ticket.service.generic;

import com.example.ticket.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {
    
    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getEntityName();
    
    @Override
    public T create(T entity) {
        return getRepository().save(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getRepository().findAll();
    }
   
    @Override
    public T update(ID id, T entity) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(
                getEntityName() + " avec l'ID " + id + " non trouvé");
        }
        return getRepository().save(entity);
    }
    
    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(
                getEntityName() + " avec l'ID " + id + " non trouvé");
        }
        getRepository().deleteById(id);
    }
    
    protected T findByIdOrThrow(ID id) {
        return getRepository().findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                getEntityName() + " avec l'ID " + id + " non trouvé"));
    }
}