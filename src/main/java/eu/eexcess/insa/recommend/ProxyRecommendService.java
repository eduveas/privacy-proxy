package eu.eexcess.insa.recommend;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpOperationFailedException;

import eu.eexcess.insa.camel.JsonXMLDataFormat;
import eu.eexcess.insa.proxy.JSONList2JSON;
import eu.eexcess.insa.proxy.actions.ApplyPrivacySettingsJS;
import eu.eexcess.insa.proxy.actions.EnrichedRecommendationQueryAggregator;
import eu.eexcess.insa.proxy.actions.ExtractUserEnvironment;
import eu.eexcess.insa.proxy.actions.PrepareRecommendationTermsPonderation;
import eu.eexcess.insa.proxy.actions.PrepareRecommendationTracesRequest;
import eu.eexcess.insa.proxy.actions.UserProfileEnricherAggregator;
import eu.eexcess.insa.proxy.connectors.CloseJsonObject;
import eu.eexcess.insa.proxy.connectors.EconBizQueryMapper;
import eu.eexcess.insa.proxy.connectors.EconBizResultFormater;
import eu.eexcess.insa.proxy.connectors.MendeleyDocumentQueryMapper;
import eu.eexcess.insa.proxy.connectors.MendeleyQueriesAggregator;
import eu.eexcess.insa.proxy.connectors.MendeleyQueryMapper;
import eu.eexcess.insa.proxy.connectors.RecomendationResultAggregator;

public class ProxyRecommendService extends RouteBuilder {
	final PrepareRecommendationTermsPonderation prepPonderation = new PrepareRecommendationTermsPonderation();
	final EnrichedRecommendationQueryAggregator recommendationQueryAggregator = new EnrichedRecommendationQueryAggregator();
	final ExtractUserEnvironment extractUserEnv = new ExtractUserEnvironment();
	final UserProfileEnricherAggregator userContextAggregator = new UserProfileEnricherAggregator();
	final PrepareRecommendationTracesRequest prepTraces = new PrepareRecommendationTracesRequest();
	final EconBizQueryMapper prepEconBizQuery = new EconBizQueryMapper ();
	final MendeleyQueryMapper prepMendeleyQuery = new MendeleyQueryMapper();
	final MendeleyDocumentQueryMapper prepDocumentSearch = new MendeleyDocumentQueryMapper();
	final EconBizResultFormater econBizResultFormater = new EconBizResultFormater();
	final CloseJsonObject closeJson = new CloseJsonObject();


	/*
	public APIRecommendation() {
		// TODO Auto-generated constructor stub
	}

	public APIRecommendation(CamelContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	*/
	
	String apiBaseURI = // "jetty:http://localhost:12564";
			"servlet://";
	
	
	@Override
	public void configure() throws Exception {
		final ApplyPrivacySettingsJS applyPrivacySettings = new ApplyPrivacySettingsJS();
		// TODO Auto-generated method stub
		
		
		/*=========================================================================
		 *  Recommendation routes
		 *=========================================================================*/
		
		
		from(apiBaseURI + "api/v0/query/enrich")
			.to("direct:query.enrich")
		;
		
		
		/*
		 *  Route to get recommendations 
		 */
		
		from(apiBaseURI + "api/v0/recommend")
			.to("direct:recommendation.route")
		;
		
		

	}

}
