package com.jeanwolff.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeanwolff.cursomc.domain.ItemPedido;
import com.jeanwolff.cursomc.domain.PagamentoComBoleto;
import com.jeanwolff.cursomc.domain.Pedido;
import com.jeanwolff.cursomc.domain.enums.EstadoPagamento;
import com.jeanwolff.cursomc.repositories.ItemPedidoRepository;
import com.jeanwolff.cursomc.repositories.PagamentoRepository;
import com.jeanwolff.cursomc.repositories.PedidoRepository;
import com.jeanwolff.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {


	@Autowired
	private PedidoRepository repo;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	
	
	
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE.getCodigo());
		pedido.getPagamento().setPedido(pedido);
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		} 
		pedido = repo.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());

		for (ItemPedido ip: pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		return pedido;
	}
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+ id +", Tipo: "+ Pedido.class.getSimpleName()));
	}

}
