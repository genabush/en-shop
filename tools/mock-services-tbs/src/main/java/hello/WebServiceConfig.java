package hello;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

/**
 * @author vasanthramprakasam
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "placeOrder")
	public EmptyRequestSuffixWsdlDefinition defaultWsdl11DefinitionPlaceOrder(XsdSchemaCollection placeOrderSchema) {
		EmptyRequestSuffixWsdlDefinition wsdl11Definition = new EmptyRequestSuffixWsdlDefinition();
		wsdl11Definition.setPortTypeName("PlaceOrderPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setRequestSuffix("");
		wsdl11Definition.setTargetNamespace("http://placeOrder.v1.ws.mercury.com");
		wsdl11Definition.setSchemaCollection(placeOrderSchema);
		return wsdl11Definition;
	}

	@Bean(name = "order")
	public DefaultWsdl11Definition defaultWsdl11DefinitionOrder(XsdSchema orderSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("OrderPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://thebodyshop/orderSchema");
		wsdl11Definition.setSchema(orderSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema orderSchema() {
		return new SimpleXsdSchema(new ClassPathResource("Order.xsd"));
	}

	@Bean
	public XsdSchemaCollection placeOrderSchema() {
		CommonsXsdSchemaCollection xsds = new CommonsXsdSchemaCollection(new ClassPathResource("IntegrationPlaceOrder.xsd"));
		xsds.setInline(true);
		return xsds;
	}
}
