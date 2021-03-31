# SERVLET TUTORIAL
Exemplo prático utilizado para um tutorial que criei no medium.

Link Externo: https://felipece7.medium.com/utilizando-servlet-puro-82e55ab32fbe

## Persistindo dados com JPA+Servlets

De um ano pra cá venho estudando desenvolvimento de sistemas web, nos últimos meses tenho me aventurando no SERVLET. Em um certo momento cheguei a fazer uma indagação para mim mesmo, será que estudar uma tecnologia legada para quem está praticamente iniciando não seria uma perda de tempo? Agora ao meu ver, não é! Tentarei explicar um pouco do que eu entendi sobre servlet, portanto sinta-se livre para comentar qualquer coisa (dicas, sugestões, correções). Obs: O exemplo prático utilizado é bem simples.
Em janeiro de 2020 eu retornei a estudar JAVA utilizando alguns frameworks, dentre eles: springboot e Spring DataJPA. Desenvolvi um sistema bem simples de nutrição no qual está no meu repositório do github.
Inicialmente dois motivos me levaram a também estudar servlet, um é o fato de que eu não o conhecia bem e sentia que devia aprender algo sobre :), o outro e mais importante … eu queria trabalhar com as requisições e resposta mais de perto. No JAVA WEB tudo possui Servlet, os frameworks mais conhecidos utilizam api servlet por debaixo dos panos, o JavaServer Faces ou o famoso JSF, por exemplo, usa o FacesServlet em seu controlador. Uma das diferenças é que vai haver um alto nível de abstração no tratamento do HTTP request/response, logo o JSF possui uma gama de conversores, validadores prontos que irá auxiliar no desenvolvimento além do mesmo cuidar do ciclo de vida do servlet.

### Arquitetura do Servlet

<img src="https://miro.medium.com/max/700/0*ekKkBHtFr8b47CYQ.png" />

O servlet trabalha basicamente com dois objetos que são do tipo HttpServletResponse e HttpServletRequest, quando o cliente que encontra-se no lado do navegador solicita através da URL o acesso ao servlet o container irá criar dois objetos, um para HttpServletRequest com dados/parâmetros da requisição e e outro para HttpServletResponse com o corpo da resposta.

### VAMOS PRA PRÁTICA!

Vamos agora para a prática para ver um servlet em funcionamento.
Vou utilizar as seguintes tecnologias para a criação do projeto.

<ul>
  <li>JPA</li>
  <li>Servlet</li>
  <li>JSP</li>
  <li>Postgres</li>
  <li>JSTL</li>
 </ul>

Para esse exemplo foi utilizado o Eclipse, mas nada impede a utilização de uma outra IDE.

### Criação do Dynamic Web Project

<img src="https://miro.medium.com/max/581/1*QwbJBqm8SuKSlJD2r5LsJQ.png" />

Podemos prosseguir com a criação do projeto no botão next, até chegar na ultima tela de configuração, aonde será marcado a geração do web.xml.

<img src="https://miro.medium.com/max/508/1*snbgOUw8VYpI_1AUJWqJIg.png" />

### Conversões do Projeto

Para dar prosseguimento ao projeto será necessário dizer que o dynamic web project também é um projeto maven. O eclipse automatiza tudo na opção Configure.

<img src="https://miro.medium.com/max/657/1*t3f_IWQ8EVDCTg37xGbhtg.png" />

Depois é preciso converter o projeto para um Project Facets JPA. Que se localiza no caminho parecido com o da imagem acima.

Configure >> Convert to JPA Project

<img src="https://miro.medium.com/max/700/1*nd0l7VjyMEn6zAlozKrRAQ.png" />

### Configuração do persistence.xml

Feito isso vamos configurar nossa fábrica de persistência da seguinte maneira … sempre passando um provider e a configuração detalhada de conexão com o banco de dados postgres.

<em>persistence.xml</em>

```
<persistence-unit name=”c_jpa”>
<provider>org.hibernate.ejb.HibernatePersistence</provider><class>servlet.model.Usuario</class>
<properties>
<property name=”hibernate.show_sql” value=”true” />
<property name=”javax.persistence.jdbc.driver” value=”org.postgresql.Driver” />
<property name=”javax.persistence.jdbc.url” value=”jdbc:postgresql://localhost:5432/teste” />
<property name=”javax.persistence.jdbc.user” value=”postgres” /><property name=”javax.persistence.jdbc.password” value=”root” />
<property name=”hibernate.dialect” value=”org.hibernate.dialect.PostgreSQLDialect” />
<property name=”hibernate.hbm2ddl.auto” value=”update” />
</properties>
</persistence-unit>
```

### Configuração do Maven

É necessário acrescentar as seguintes dependências:

<em>pom.xml</em>

```
<dependency>
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<version>42.2.18</version>
</dependency>
<dependency>
<groupId>org.hibernate.javax.persistence</groupId>
<artifactId>hibernate-jpa-2.0-api</artifactId><version>1.0.1.Final</version>
</dependency>
<dependency>
<groupId>javax.servlet</groupId>
<artifactId>javax.servlet-api</artifactId>
<version>4.0.1</version>
<scope>provided</scope>
</dependency>
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-core</artifactId>
<version>5.4.28.Final</version>
</dependency>
<dependency>
<groupId>jstl</groupId>
<artifactId>jstl</artifactId>
<version>1.2</version>
</dependency>
```

### Criando o código fonte

Na pasta de código fonte criei os seguintes pacotes: servlet.model, servlet.servlets, servlet.dao, servlet.util.

No pacote util vai ter a criação de um classe chamada JpaUtil com o padrão singleton, que visa instanciar um único EntityManager e utiliza-lo sempre que for preciso enquanto o mesmo estiver aberto.

<em>JpaUtil.java</em>

```
package servlet.util;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class JpaUtil {
private static EntityManager em=null;
private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("c_jpa");
static {
pegarConexao();
}
public static EntityManager pegarConexao() {
if(em==null) {
em = emf.createEntityManager();
}
return em;
}
```

Na camada modelo vamos criar a classe entidade básica para Usuário, utilizando as seguintes anotações: @Entity, @Id, @GeneratedValue

<em>Usuario.java</em>

```
package servlet.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity(name = "usuario")
public class Usuario {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String nome;
public Long getId() {
return id;
}
public void setId(Long id) {
this.id = id;
}
public String getNome() {
return nome;
}
public void setNome(String nome) {
this.nome = nome;
}
}
```

Agora para fazer a persistência no banco de dados será utilizado o padrão DAO Generics.

<em>DaoGenerico.java</em>

```
package servlet.dao;
import javax.persistence.EntityManager;
import servlet.util.JpaUtil;
public class DaoGenerico<T> {
private EntityManager em = JpaUtil.pegarConexao();
public void cadastrar(T t) {
em.getTransaction().begin();
em.persist(t);
em.getTransaction().commit();
}
}
```

Não entrarei em detalhes sobre como utilizar a biblioteca de persistência, em um outro artigo pretendo falar. A documentação do mesmo pode ser rapidamente encontrado aqui.

### Criando a View Inicial

Na pasta WebContent foi criado um arquivo index.jsp bem simples por sinal, com o seguinte código:

<em>index.jsp</em>

```
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url value="/usuario" var="link" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Titulo Aqui!</title>
</head>
<body>
<form method="post" action="${link}">
<label for="nome">Nome do Usuário</label> <input type="text" id="nome"
name="nome"></input> <input type="submit" value="Cadastrar"></input>
</form>
</body>
</html>
```

É possível chamar o JSTL para a página JSP através do seguinte cabeçalho:

```
<%@ taglib uri=”http://java.sun.com/jsp/jstl/core" prefix=”c”%>
```

O method vai ser o POST, o mapeamento do servlet será “/usuario”. Ao submeter o formulário será enviado a requisição para o servlet responsável, contendo inclusive o parâmetro nome com seu respectivo valor no qual foi digitado no inputText de id #nome.

### Criando o SERVLET

Para o funcionamento básico do servlet é necessário sobrescrever pelo menos um desses 3 métodos: doPost(HttpServletRequest request, HttpServletResponse response) , doGet(HttpServletRequest request, HttpServletResponse response) ou service(HttpServletRequest request, HttpServletResponse response). Já que no formulário dentro do JSP foi utilizado o POST então usei o doPost, mas também poderia ser utilizado o service(), pois ele pode tratar requisições HTTP dos métodos POST e GET.

Segue o código da classe servlet que será utilizada.

<em>UsuarioServlet.java</em>

```
package servlet.servlets;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.dao.DaoGenerico;
import servlet.model.Usuario;
@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  String nomeUsuario = request.getParameter("nome");
  Usuario u = new Usuario();
  DaoGenerico<Usuario> daoUsuario = new DaoGenerico<>();
  u.setNome(nomeUsuario);
  daoUsuario.cadastrar(u);
  request.setAttribute("nome", nomeUsuario);
  RequestDispatcher rd = request.getRequestDispatcher("/usuariocadastrado.jsp");
  rd.forward(request, response);
 }
}
```

<ul>
  <li>A anotação @WebServlet marca tal classe como um Servlet e seu url pattern será /usuario, ou seja quando tiver uma chamada na URL com esse mapeamento o container se responsabilizará por encaminhar a requisição para esse Servlet.</li>
  <li>Através do objeto request do tipo HttpServletRequest, é possível capturar o parâmetro que foi passado no corpo da requisição através do método getParameter.</li>
  <li>É instanciado um objeto Usuario e setado com o valor que foi recebido, e depois utilizado no Dao Genérico para realizar a persistência na base teste do banco de dados.</li>
  <li>Para exibir uma mensagem de sucesso simples, anexei um Atributo para a requisição através do método setAttribute().</li>
  <li>Depois é criado um objeto Dispachador que irá reenchaminhar a request para a página usuariocadastro.jsp, inclusive com o novo atributo nome.</li>
</ul>

### Criando a View de Sucesso

<em>usuariocadastrado.jsp</em>

```
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Usuário cadastrado com sucesso</title>
</head>
<body>
Usuário: <c:out value="${nome}"></c:out> cadastrado com sucesso!</body>
</html>
```

O <c:out> serve para escrever algo com o JSTL, sempre é possível pegar um atributo passado através da Linguagem de Expressão do JSTL ${}. Existe diversas outras tags para trabalhar com o JSTL, por exemplo, foreach.

### Finalização

Nesse pequeno tutorial foi criado rapidamente um cadastro bem simples, que também pode ser encontrado no meu repositório, clicando aqui.
