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
