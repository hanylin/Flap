package me.yifeiyuan.flap;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static me.yifeiyuan.flap.Preconditions.checkNotNull;

/**
 * Created by 程序亦非猿
 */
public class FlapAdapter extends RecyclerView.Adapter<FlapViewHolder> implements ItemFactoryManager {

    @NonNull
    private Flap flap = Flap.getDefault();

    private LifecycleOwner lifecycleOwner;

    @NonNull
    private List<?> models = new ArrayList<>();

    public void setLifecycleOwner(final LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setFlap(@NonNull final Flap flap) {
        checkNotNull(flap);
        this.flap = flap;
    }

    @NonNull
    @Override
    public FlapViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return flap.onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull final FlapViewHolder holder, final int position) {
        //ignore
    }

    @Override
    public void onBindViewHolder(@NonNull final FlapViewHolder holder, final int position, @NonNull final List<Object> payloads) {
        if (null != lifecycleOwner) {
            lifecycleOwner.getLifecycle().addObserver(holder);
        }
        holder.bindData(getModel(position), this, payloads);
    }

    @Override
    public int getItemViewType(final int position) {
        return flap.getItemViewType(getModel(position));
    }

    @Override
    public void registerItemFactory(@NonNull final ItemFactory itemFactory) {
        flap.registerItemFactory(itemFactory);
    }

    @Override
    public void unregisterItemFactory(@NonNull final ItemFactory itemFactory) {
        flap.unregisterItemFactory(itemFactory);
    }

    private Object getModel(final int position) {
        return getModels().get(position);
    }

    @Override
    public int getItemCount() {
        List<?> models = getModels();
        if (models == null || models.isEmpty()) {
            return 0;
        }
        return models.size();
    }

    public void setModels(@NonNull List<?> models) {
        checkNotNull(models);
        this.models = models;
    }

    @NonNull
    public List<?> getModels() {
        return models;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView.getContext() instanceof LifecycleOwner && lifecycleOwner == null) {
            setLifecycleOwner((LifecycleOwner) recyclerView.getContext());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FlapViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FlapViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(@NonNull FlapViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}