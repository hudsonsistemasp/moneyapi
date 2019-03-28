package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@RestController //Essa classe é um controlador, que recebe requisições, tneho que apontar para o SPRING quem fará esse papel com essa notação
//então vou usar o postman para fazê-las(simulando um frontend)
@RequestMapping("/categorias")//notação que aponta a URL que receberá a requisição, mapeando a requisição
public class CategoriaResource {
	@Autowired //Notação que faz a injeção de dependência e com isso conseguimos usar os métodos da interface CategoriaRepository.java
	private CategoriaRepository categoriaRepository;
	
	//Aqui é usada a teoria do retorno de coleção vazia e qual status enviar para o usuário
	//Se a coleção retorna vazia, virá o cod 204 ok com o método noContent(), mas se for o notFound(), virá 404, mas a coleção existe e
	//Dai pode dar a enteder que a URL está errada, ou foi modificada e etc...
	/*@GetMapping 
	public ResponseEntity<?> listar(){
		List<Categoria> categorias = categoriaRepository.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
	}*/
	
	//Aqui retorna 200ok para o usuário indpendente da coleção vazia ou não
	@GetMapping
	public List<Categoria> listar(){ 
		return categoriaRepository.findAll();
	}
	
	//Quando eu crio um recurso no banco de dados, o REST diz que no HEADER de resposta eu devo informar os dados para localizar esse recurso que foi criado
	//, no location que está dentro do HEADER. Pra isso preciso receber um HttpServletResponse para poder trabalhar com este HEADER
	
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED)//O cod. ideal para o usuário é o 201created, para isso usa essa notação, 
	//mas foi substituída pela linha do return abaixo, pois já estou apontando o status de CREATED
	//@Valid é notação para validar os campos que estão no parâmetro e que possuem indicação na classe MODEL
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		//Para eu pegar o código da categoria salva
		Categoria categoriaSalva =  categoriaRepository.save(categoria);
		//Agora usar uma classe do SPRING que recupera a informação à partir da Uri da requisição atual
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(categoriaSalva.getCodigo()).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSalva);//Aqui eu consigo retornar mais informações do que foi salva para quem está consumindo a API
		//, algum calculo no servidor e mais dados atrelado ao recurso criado e etc..
		
	}
	
	/*@GetMapping("/{codigo}")//Coloca a notação PathVariable para apontar que a variável é do path URI ACIMA
	public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
		return categoriaRepository.findOne(codigo);
	}*/
	@GetMapping("/{codigo}")//Coloca a notação PathVariable para apontar que a variável é do path URI ACIMA
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable long codigo){
		Categoria categorias = categoriaRepository.findOne(codigo);
		return categorias != null ? ResponseEntity.ok(categorias) : ResponseEntity.notFound().build();
	}
	
	
	/*Existe uma situação que o cliente pode passar algumas propriedades que não estão contidas no JSON que apontará para a classe categoria que são Codigo e nome,
	  mas pode ser passada a propriedade, no JSON, como exemplo "descrição, "outra observação" e isso não está contido na classe. No arquivo APPLICATION.PROPERTIES 
	  podemos colocar uma linha de configuração que retornará um log de erro informando isso ao usuário, ou se não tiver ele somente escreverá a propriedade JSON que
	  existe na classe e não dará log para o usuário. Mais para titulo de mensagem mesmo para o cliente: Jackson é a biblioteca que transforma JAVA em JSON e vice-versa
	  spring.jackson.deserialization.fail-on-unknown-properties=true 
	*/
}






