package hyeok.board.common.outboxmessagerelay;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AssignedShardTest {
    @Test
    void of() {
        // given
        Long shardCount = 64L;

        List<String> apps = List.of("appId1", "appId2", "appId3");
        // when

        AssignedShard assignedShard1 = AssignedShard.of(apps.get(0), apps, shardCount);
        AssignedShard assignedShard2 = AssignedShard.of(apps.get(1), apps, shardCount);
        AssignedShard assignedShard3 = AssignedShard.of(apps.get(2), apps, shardCount);
        AssignedShard assignedShard4 = AssignedShard.of("invalid", apps, shardCount);

        // then
        List<Long> result = Stream.of(assignedShard1.shards(), assignedShard2.shards(), assignedShard3.shards(), assignedShard4.shards())
                .flatMap(List::stream)
                .toList();

        assertThat(result).hasSize(shardCount.intValue());


        for (int i = 0; i < 64; i++) {
            assertThat(result.get(i)).isEqualTo(i);
        }

        assertThat(assignedShard4.shards()).isEmpty();
    }
}