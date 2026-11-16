package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Usuario;
import Repositories.UsuarioRepository;

public class UsuariosController extends GenericController<Usuario>
{
    public UsuariosController() throws RuntimeException
    {
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
    protected void addRepository(Usuario element) throws RepositoryWriteException
    {
        new UsuarioRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Usuario element) throws RepositoryWriteException
    {
        new UsuarioRepository().update(element);
    }
}
