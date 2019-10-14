package mhe;

import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import mhe.compiler.MheCompiler;
import mhe.compiler.exception.CompilerException;
import mhe.logic.builder.AbstractBuilder;
import mhe.logic.exception.LogicException;
import mhe.service.AbstractLogicService;

public class MheVerticle extends AbstractVerticle {

    /** VertX Router */
    private Router router = null;

    /** HTTP Server */
    private HttpServer server = null;

    /** Body Handler */
    private BodyHandler bodyHandler = null;

    /** Cors Handler */
    private CorsHandler corsHandler = null;

    /** Timeout Handler */
    private TimeoutHandler timeoutHandler = null;

    /** Compiler */
    private MheCompiler compiler;

    /** Service */
    private LogicService service;

    @Override
    public void start() {
        List<String> args = this.processArgs();

        System.out.println("[MHE] Starting verticle... " + args.toString());

        try {
            compiler = new MheCompiler();
            service = new AbstractLogicService(new AbstractBuilder());
            server = vertx.createHttpServer();
            router = Router.router(vertx);

            bodyHandler = BodyHandler.create();
            corsHandler = CorsHandler.create("*");
            timeoutHandler = TimeoutHandler.create(30000);

            corsHandler.allowedMethod(HttpMethod.GET);
            corsHandler.allowedMethod(HttpMethod.POST);
            corsHandler.allowedMethod(HttpMethod.PUT);
            corsHandler.allowedMethod(HttpMethod.DELETE);
            corsHandler.allowedHeader("Authorization");
            corsHandler.allowedHeader("Content-Type");

            router.route().handler(bodyHandler);
            router.route().handler(corsHandler);
            router.route().handler(timeoutHandler);

            router.route(HttpMethod.POST, "/logic-expression-to-expression-tree")
            .consumes("application/json")
            .produces("application/json")
            .handler(this.postLogicExpressionToExpressionTree());

            router.route(HttpMethod.POST, "/expression-tree-to-truth-table")
            .consumes("application/json")
            .produces("application/json")
            .handler(this.postExpressionTreeToTruthTable());

            router.route(HttpMethod.POST, "/truth-table-to-decision-tree")
            .consumes("application/json")
            .produces("application/json")
            .handler(this.postTruthTableToDecisionTree());

            server.requestHandler(router::accept).listen(8081);

            System.out.println("[MHE] Listen...");
        }
        catch(Exception ex){
            System.out.println("[MHE] Error! " + ex.toString());
            ex.printStackTrace();
        }
    }

    public Handler<RoutingContext> postLogicExpressionToExpressionTree(){
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", "application/json");

            try {
                JsonObject requestPayload = request.getBodyAsJson();
                String logicNode = compiler.expressionToJson(requestPayload.getString("expression"));

                JsonObject responsePayload = new JsonObject().put("expressionTree", new JsonObject(logicNode));
                response.setStatusCode(200);
                response.end(responsePayload.toBuffer());
            }
            catch(CompilerException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            }
            catch(Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }

    public Handler<RoutingContext> postExpressionTreeToTruthTable(){
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", "application/json");

            try {
                JsonObject expressionTree = request.getBodyAsJson().getJsonObject("expressionTree");
                JsonObject responsePayLoad = this.service.fromExpressionTreeToTruthTable(expressionTree);

                response.setStatusCode(200);
                response.end(responsePayLoad.toBuffer());
            }
            catch(LogicException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            }
            catch(Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }

    public Handler<RoutingContext> postTruthTableToDecisionTree(){
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", "application/json");

            try {
                JsonObject requestPayload  = request.getBodyAsJson().getJsonObject("truthTable");
                JsonObject responsePayLoad = this.service.fromTruthTableToDecisionTree(requestPayload);
                response.setStatusCode(200);
                response.end(responsePayLoad.toBuffer());
            }
            catch(LogicException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            }
            catch(Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }
}
