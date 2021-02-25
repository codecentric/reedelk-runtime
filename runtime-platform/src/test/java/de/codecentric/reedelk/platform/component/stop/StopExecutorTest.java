package de.codecentric.reedelk.platform.component.stop;

import de.codecentric.reedelk.platform.execution.AbstractExecutionTest;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class StopExecutorTest extends AbstractExecutionTest {

    private StopExecutor executor = new StopExecutor();

    @Test
    void shouldReturnOriginalPublisher() {
        // Given
        ExecutionGraph graph = ExecutionGraph.build();
        ExecutionNode stop = newExecutionNode(new Stop());
        Publisher<MessageAndContext> publisher = Mono.empty();

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, stop, graph);

        // Then
        assertThat(publisher).isEqualTo(endPublisher);
    }
}
