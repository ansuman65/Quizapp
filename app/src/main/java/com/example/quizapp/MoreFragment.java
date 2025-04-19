// MoreFragment.java
package com.example.quizapp;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("⚙️ More features soon");
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
