package mhe.compiler.xson;

import java.util.Collection;
import java.util.List;

import mhe.compiler.model.AbstractSintaxTree;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;

public class XsonSintaxTree implements AbstractSintaxTree<XsonTreeCategory> {

    @Override
    public int getSerial() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getShape() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int compareTo(GraphVizNode o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isLambda() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public XsonTreeCategory getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<AbstractSintaxTree<XsonTreeCategory>> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractSintaxTree<XsonTreeCategory> getFirstChild() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractSintaxTree<XsonTreeCategory> getSecondChild() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        // TODO Auto-generated method stub
        return null;
    }

}
