package com.reedelk.esb.flow.deserializer;

import com.reedelk.esb.flow.deserializer.converter.DeserializerConverterContextDecorator;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.module.DeSerializedModule;
import com.reedelk.esb.module.ModulesManager;
import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.component.Stop;
import com.reedelk.runtime.converter.DeserializerConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDeserializerTest {

    final long moduleId = 10L;

    protected final String component1Name = TestComponent.class.getName() + "1";
    protected final String component2Name = TestComponent.class.getName() + "2";
    protected final String component3Name = TestComponent.class.getName() + "3";
    protected final String component4Name = TestComponent.class.getName() + "4";
    protected final String component5Name = TestComponent.class.getName() + "5";
    protected final String component6Name = TestComponent.class.getName() + "6";
    protected final String component7Name = TestComponent.class.getName() + "7";
    protected final String component8Name = TestComponent.class.getName() + "8";

    @Mock
    protected Bundle bundle;
    @Mock
    protected ExecutionGraph graph;
    @Mock
    protected ExecutionNode parent;
    @Mock
    protected ExecutionNode component1;
    @Mock
    protected ExecutionNode component2;
    @Mock
    protected ExecutionNode component3;
    @Mock
    protected ExecutionNode component4;
    @Mock
    protected ExecutionNode component5;
    @Mock
    protected ExecutionNode component6;
    @Mock
    protected ExecutionNode component7;
    @Mock
    protected ExecutionNode component8;
    @Mock
    private ModulesManager mockModulesManager;
    @Mock
    private DeSerializedModule mockDeSerializedModule;

    protected FlowDeserializerContext context;

    protected ExecutionNode stopNode1 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new Stop()));
    protected ExecutionNode stopNode2 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new Stop()));

    @BeforeEach
    public void setUp() {
        DeserializerConverter deserializerConverter = DeserializerConverter.getInstance();
        deserializerConverter = new DeserializerConverterContextDecorator(deserializerConverter, moduleId);
        context = spy(new FlowDeserializerContext(bundle, mockModulesManager, mockDeSerializedModule, deserializerConverter));

        lenient().doReturn(new TestComponent()).when(component1).getComponent();
        lenient().doReturn(new TestComponent()).when(component2).getComponent();
        lenient().doReturn(new TestComponent()).when(component3).getComponent();
        lenient().doReturn(new TestComponent()).when(component4).getComponent();
        lenient().doReturn(new TestComponent()).when(component5).getComponent();
        lenient().doReturn(new TestComponent()).when(component6).getComponent();
        lenient().doReturn(new TestComponent()).when(component7).getComponent();
        lenient().doReturn(new TestComponent()).when(component8).getComponent();

        lenient().doReturn(stopNode1, stopNode2).when(context).instantiateComponent(Stop.class);
        lenient().doReturn(component1).when(context).instantiateComponent(component1Name);
        lenient().doReturn(component2).when(context).instantiateComponent(component2Name);
        lenient().doReturn(component3).when(context).instantiateComponent(component3Name);
        lenient().doReturn(component4).when(context).instantiateComponent(component4Name);
        lenient().doReturn(component5).when(context).instantiateComponent(component5Name);
        lenient().doReturn(component6).when(context).instantiateComponent(component6Name);
        lenient().doReturn(component7).when(context).instantiateComponent(component7Name);
        lenient().doReturn(component8).when(context).instantiateComponent(component8Name);
    }
}
