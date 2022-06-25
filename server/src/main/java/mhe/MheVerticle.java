package mhe;

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
import java.util.List;
import mhe.compiler.MheCompiler;
import mhe.compiler.exception.CompilerException;
import mhe.logic.builder.AbstractBuilder;
import mhe.logic.exception.LogicException;
import mhe.service.AbstractLogicService;

/**
 * MheVerticle.
 */
public class MheVerticle extends AbstractVerticle {
    private static final String APPLICATION_JSON = "application/json";
    /**
     * Compiler.
     */
    private final MheCompiler compiler = new MheCompiler();

    /**
     * Service.
     */
    private final LogicService service = new AbstractLogicService(new AbstractBuilder());

    @Override
    public void start() {
        List<String> args = this.processArgs();

        System.out.println("[MHE] Starting verticle... " + args.toString());

        try {
            CorsHandler corsHandler = CorsHandler.create("*");
            corsHandler.allowedMethod(HttpMethod.GET);
            corsHandler.allowedMethod(HttpMethod.POST);
            corsHandler.allowedMethod(HttpMethod.PUT);
            corsHandler.allowedMethod(HttpMethod.DELETE);
            corsHandler.allowedHeader("Authorization");
            corsHandler.allowedHeader("Content-Type");

            Router router = Router.router(vertx);
            BodyHandler bodyHandler = BodyHandler.create();
            TimeoutHandler timeoutHandler = TimeoutHandler.create(30000);

            router.route().handler(bodyHandler);
            router.route().handler(corsHandler);
            router.route().handler(timeoutHandler);

            router.route(HttpMethod.POST, "/logic-expression-to-expression-tree")
                    .consumes(APPLICATION_JSON)
                    .produces(APPLICATION_JSON)
                    .handler(this.postLogicExpressionToExpressionTree());

            router.route(HttpMethod.POST, "/expression-tree-to-truth-table")
                    .consumes(APPLICATION_JSON)
                    .produces(APPLICATION_JSON)
                    .handler(this.postExpressionTreeToTruthTable());

            router.route(HttpMethod.POST, "/truth-table-to-decision-tree")
                    .consumes(APPLICATION_JSON)
                    .produces(APPLICATION_JSON)
                    .handler(this.postTruthTableToDecisionTree());

            router.route(HttpMethod.POST, "/decision-tree-to-expression-tree")
                    .consumes(APPLICATION_JSON)
                    .produces(APPLICATION_JSON)
                    .handler(this.postDecisionTreeToExpressionTree());

            HttpServer server = vertx.createHttpServer();
            server.requestHandler(router::accept).listen(8081);

            System.out.println("[MHE] Listen...");
        } catch (Exception ex) {
            System.out.println("[MHE] Error! " + ex.toString());
            ex.printStackTrace();
        }
    }

    private Handler<RoutingContext> postLogicExpressionToExpressionTree() {
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", APPLICATION_JSON);

            try {
                JsonObject requestPayload = request.getBodyAsJson();
                String logicNode = compiler.expressionToJson(requestPayload.getString("expression"));

                JsonObject responsePayload = new JsonObject().put("expressionTree", new JsonObject(logicNode));
                response.setStatusCode(200);
                response.end(responsePayload.toBuffer());
            } catch (CompilerException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            } catch (Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }

    private Handler<RoutingContext> postExpressionTreeToTruthTable() {
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", APPLICATION_JSON);

            try {
                JsonObject responsePayLoad = this.service.fromExpressionTreeToTruthTable(request.getBodyAsJson());
                response.setStatusCode(200);
                response.end(responsePayLoad.toBuffer());
            } catch (LogicException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            } catch (Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }

    private Handler<RoutingContext> postTruthTableToDecisionTree() {
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", APPLICATION_JSON);

            try {
                JsonObject responsePayLoad = this.service.fromTruthTableToDecisionTree(request.getBodyAsJson());
                response.setStatusCode(200);
                response.end(responsePayLoad.toBuffer());
            } catch (LogicException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            } catch (Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }

    private Handler<RoutingContext> postDecisionTreeToExpressionTree() {
        return request -> {
            HttpServerResponse response = request.response();
            JsonObject payload = new JsonObject();
            response.putHeader("content-type", APPLICATION_JSON);

            try {
                JsonObject responsePayLoad = this.service.fromDecisionTreeToExpressionTree(request.getBodyAsJson());
                response.setStatusCode(200);
                response.end(responsePayLoad.toBuffer());
            } catch (LogicException ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(400);
                response.end(payload.toBuffer());
            } catch (Exception ex) {
                ex.printStackTrace();
                payload.put("status", "ko");
                payload.put("error", ex.toString());
                response.setStatusCode(500);
                response.end(payload.toBuffer());
            }
        };
    }
}
