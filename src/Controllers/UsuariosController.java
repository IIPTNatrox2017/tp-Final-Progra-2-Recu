package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Usuario;
import Repositories.UsuarioRepository;

public class UsuariosController extends GenericController<Usuario>
{
    private final PersonasController personas;

    public UsuariosController(PersonasController personas) throws RuntimeException
    {
        this.personas = personas;
        this.add(new UsuarioRepository().selectAll());
    }
    
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(p -> p.getIdUsuario().equals(id));
    }
    
    @Override
    public Usuario get(Integer id)
    {
        return this.elementos.stream().filter(p -> p.getIdUsuario().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        Usuario usuario = this.get(id);
        
        if(usuario == null)
        {
            return false;
        }
        
        new UsuarioRepository().delete(usuario);
        
        this.elementos.remove(usuario);
        
        return true;
    }
    
    @Override
    protected Usuario addRepository(Usuario element) throws RepositoryWriteException
    {
        if (this.personas.exists(element.getDni()))
        {
            throw new RepositoryWriteException("Ya existe una persona con el DNI ingresado");
        }

        return new UsuarioRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Usuario element) throws RepositoryWriteException
    {
        new UsuarioRepository().update(element);
    }
}
