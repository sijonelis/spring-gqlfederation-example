package com.gqlfederationexample.user.domain.dataloader;

import com.gqlfederationexample.configuration.otel.DgsDataLoaderAsyncExecutor;
import com.gqlfederationexample.user.domain.model.User;
import com.gqlfederationexample.user.system.QueryDispatcher;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.MappedBatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "authorDl")
public class AuthorDataLoader implements MappedBatchLoader<Long, User> {

    @Autowired
    private QueryDispatcher queryDispatcher;

    @Override
    public CompletionStage<Map<Long, User>> load(Set<Long> keys) {
      // This slow operation will now run on a dedicated thread pool instead of the common pool
      return CompletableFuture.supplyAsync(() -> queryDispatcher.fetchUserMapByIdList(keys), DgsDataLoaderAsyncExecutor.get());
    }
}

