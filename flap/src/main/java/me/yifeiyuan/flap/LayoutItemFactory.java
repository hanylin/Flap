package me.yifeiyuan.flap;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import me.yifeiyuan.flap.internal.FlapItemFactory;

import static me.yifeiyuan.flap.Flap.DEFAULT_ITEM_TYPE_COUNT;

/**
 * Created by 程序亦非猿
 *
 * Please use annotation @Flap instead.
 */
@Deprecated
public abstract class LayoutItemFactory<T, VH extends FlapItem<T>> implements FlapItemFactory<T, VH> {

    private static final Map<Class<?>, Constructor> sConstructorCache = new HashMap<>(DEFAULT_ITEM_TYPE_COUNT);

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        View view = inflater.inflate(viewType, parent, false);

        Class clazz = (Class<?>) ReflectUtils.getTypes(this)[1];

        VH vh = null;
        Constructor constructor = sConstructorCache.get(clazz);
        try {
            if (constructor == null) {
                constructor = clazz.getConstructor(View.class);
                constructor.setAccessible(true);
                sConstructorCache.put(clazz, constructor);
            }
            vh = (VH) constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return vh;
    }

    @Override
    public final int getItemViewType(final T model) {
        return getLayoutResId(model);
    }

    @LayoutRes
    protected abstract int getLayoutResId(final T model);

}
