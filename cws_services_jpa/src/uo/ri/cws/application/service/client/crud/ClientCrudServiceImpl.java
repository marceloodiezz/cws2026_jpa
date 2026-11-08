package uo.ri.cws.application.service.client.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.util.exception.BusinessException;

public class ClientCrudServiceImpl implements ClientCrudService {
	
	@Override
    public ClientDto addClient(ClientDto client, String recommenderNif)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteClientByNif(String nif) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateClient(ClientDto client) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<ClientDto> findAllClients() throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<ClientDto> findClientByNif(String nif)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<ClientDto> findClientsRecommendedBy(String recommenderNif)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
