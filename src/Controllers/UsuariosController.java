package Controllers;

import Models.Usuario;

public class UsuariosController extends GenericController<Usuario>
{
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
        return this.elementos.removeIf(p -> p.getIdUsuario().equals(id));
    }
}
