package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.content.Context;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * MenuActivity is an entry point for each functionality of the Scanner.
 * <p>
 * Each functionality is handled by a specific fragment:
 * <ul>
 *   <li>{@link SymbologyFragment}</li>
 *   <li>{@link ScannerOptionsFragment}</li>
 *   <li>{@link StandardFormatterFragment}</li>
 *   <li>{@link PresentationModeFragment}</li>
 *   <li>{@link IntentWedgeFragment}</li>
 *   <li>{@link KeyboardWedgeFragment}</li>
 *   <li>{@link WebWedgeFragment}</li>
 *   <li>{@link DecodingNotificationFragment}</li>
 *   <li>{@link GoodReadFragment}</li>
 * </ul>
 * </p>
 */
public class MenuActivity extends FragmentActivity {
    private static final String TAG = MenuActivity.class.getSimpleName();
    private static final String LAST_FRAGMENT_TAG = "lastFragmentTag";
    private static final String DEFAULT_FRAGMENT_TAG = "";
    private String mLastFragmentTag = DEFAULT_FRAGMENT_TAG;
    private RecyclerView mMenu;
    private Button mMenuButton;

    private View mKitchenContent;

    private interface ClickHandler {
        void onClick();
    }

    private static abstract class MenuEntry implements ClickHandler {
        abstract String getText();
    }

    private final class OnClickMenuEntry extends MenuEntry {
        private final String mText;
        private final ClickHandler mClickHandler;

        OnClickMenuEntry(String text, ClickHandler clickHandler) {
            mText = text;
            mClickHandler = clickHandler;
        }

        @Override
        String getText() {
            return mText;
        }

        @Override
        public void onClick() {
            toggleMenuVisibility();
            mClickHandler.onClick();
        }
    }

    private final class FragmentMenuEntry<T extends Fragment> extends MenuEntry {
        private final class FragmentClassOrInstance<T extends Fragment> {
            final Class<T> mClazz;
            T mFragment = null;

            FragmentClassOrInstance(Class<T> clazz) {
                mClazz = clazz;
            }

            T getFragment() {
                if (mFragment == null) {
                    try {
                        mFragment = mClazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        Log.e(TAG, "unable to create fragment", e);
                    }
                }
                return mFragment;
            }
        }

        private final String mText;
        private final FragmentClassOrInstance<T> mFragment;

        FragmentMenuEntry(String text, Class<T> clazz) {
            mText = text;
            mFragment = new FragmentClassOrInstance<>(clazz);
        }

        @Override
        String getText() {
            return mText;
        }

        @Override
        public void onClick() {
            Fragment fragment = mFragment.getFragment();
            if (fragment != null) {
                MenuActivity.this.showFragment(fragment);
                toggleMenuVisibility();
                mLastFragmentTag = fragment.getTag();
            } else {
                Log.e(TAG, "cannot show fragment for " + getText());
            }
        }
    }
    private final List<MenuEntry> mMenuEntries = Arrays.asList(
            new FragmentMenuEntry("symbology", SymbologyFragment.class),
            new FragmentMenuEntry("scanner option", ScannerOptionsFragment.class),
            new FragmentMenuEntry("standard formatter", StandardFormatterFragment.class),
            new FragmentMenuEntry("presentation", PresentationModeFragment.class),
            new FragmentMenuEntry("intent wedge", IntentWedgeFragment.class),
            new FragmentMenuEntry("keyboard wedge", KeyboardWedgeFragment.class),
            new FragmentMenuEntry("web wedge", WebWedgeFragment.class),
            new FragmentMenuEntry("decoding notification", DecodingNotificationFragment.class),
            new FragmentMenuEntry("good read", GoodReadFragment.class));

    public MenuActivity() {
        mMenuEntries.sort(Comparator.comparing(MenuEntry::getText));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        mKitchenContent = findViewById(R.id.kitchen_content);
        mMenu = findViewById(R.id.menu);
        mMenu.setAdapter(new MenuAdapter(this));
        mMenu.setLayoutManager(new GridLayoutManager(this, 1));
        mMenuButton = findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(view -> toggleMenuVisibility());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(LAST_FRAGMENT_TAG, mLastFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // The app is being started for the first time.
        if (savedInstanceState == null) {
            return;
        }

        // The app is being reloaded, restores the last fragment UI.
        mLastFragmentTag = savedInstanceState.getString(LAST_FRAGMENT_TAG);
        if (mLastFragmentTag != DEFAULT_FRAGMENT_TAG) {
            toggleMenuVisibility();
        }
    }

    private void toggleMenuVisibility() {
        boolean menuVisible = mMenu.getVisibility() == View.VISIBLE;
        mMenu.setVisibility(menuVisible ? View.GONE : View.VISIBLE);
        mKitchenContent.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        mMenuButton.setText(menuVisible ? "Show Scanner Menu" : "Hide Scanner Menu");
    }


    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.kitchen_content, fragment)
                .commit();
    }

    private final class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }


    private final class MenuAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private final LayoutInflater mLayoutInflator;

        MenuAdapter(Context context) {
            mLayoutInflator = LayoutInflater.from(context);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflator.inflate(R.layout.menu_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.mTitle.setText(mMenuEntries.get(position).getText());
            holder.mTitle.setOnClickListener(v -> mMenuEntries.get(position).onClick());
        }

        @Override
        public int getItemCount() {
            return mMenuEntries.size();
        }
    }
}
