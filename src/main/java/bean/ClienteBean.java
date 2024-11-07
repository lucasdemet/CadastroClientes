package bean;

import dao.ClienteDAO;
import model.Cliente;
import service.ViaCEPService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cliente cliente = new Cliente();
    private String termoPesquisa;
    private List<Cliente> clientes;
    private ClienteDAO clienteDAO = new ClienteDAO();

    public ClienteBean() {
        
    }

    public void novo() {
        this.cliente = new Cliente();
    }

    public void buscarCep() {
        try {
            if (cliente.getCep() != null && !cliente.getCep().isEmpty()) {
                cliente.setCep(cliente.getCep().replace("-", "").replace(".", ""));
                ViaCEPService.Endereco endereco = ViaCEPService.buscarEnderecoPorCep(cliente.getCep());
                if (endereco != null) {
                    cliente.setEndereco(endereco.getLogradouro());
                    cliente.setBairro(endereco.getBairro());
                    cliente.setCidade(endereco.getLocalidade());
                    cliente.setEstado(endereco.getUf());

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "CEP encontrado"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar CEP", e.getMessage()));
        }
    }

    public void salvar() {
        try {
            if (clienteDAO.emailJaExiste(cliente.getEmail(), cliente.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Email já cadastrado"));
                return;
            }

            clienteDAO.salvar(cliente);

            
            if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                this.clientes = clienteDAO.buscarPorNome(termoPesquisa);
            }

            this.cliente = new Cliente();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente salvo com sucesso"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar", e.getMessage()));
        }
    }

    public void excluir(Cliente cliente) {
        try {
            clienteDAO.excluir(cliente.getId());

            
            if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                this.clientes = clienteDAO.buscarPorNome(termoPesquisa);
            } else {
                this.clientes = clienteDAO.buscarTodos();
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente excluído com sucesso"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir", e.getMessage()));
        }
    }

    public void editar(Cliente cliente) {
        
        this.cliente = new Cliente();
        this.cliente.setId(cliente.getId());
        this.cliente.setNome(cliente.getNome());
        this.cliente.setEmail(cliente.getEmail());
        this.cliente.setTelefone(cliente.getTelefone());
        this.cliente.setCep(cliente.getCep());
        this.cliente.setEndereco(cliente.getEndereco());
        this.cliente.setBairro(cliente.getBairro());
        this.cliente.setCidade(cliente.getCidade());
        this.cliente.setEstado(cliente.getEstado());
    }

    public void pesquisar() {
        try {
            if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                this.clientes = clienteDAO.buscarPorNome(termoPesquisa);
            } else {
                this.clientes = clienteDAO.buscarTodos();
            }

            if (this.clientes.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum cliente encontrado"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao pesquisar", e.getMessage()));
        }
    }

    
    public void limpar() {
        this.cliente = new Cliente();
        this.termoPesquisa = null;
        this.clientes = null;
    }

    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTermoPesquisa() {
        return termoPesquisa;
    }

    public void setTermoPesquisa(String termoPesquisa) {
        this.termoPesquisa = termoPesquisa;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}