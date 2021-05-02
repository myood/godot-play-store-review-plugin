package com.zywegry.godotplaystorereview;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.SignalInfo;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

public class GodotPlayStoreReview extends org.godotengine.godot.plugin.GodotPlugin {

    ReviewManager review_manager = null;

    public GodotPlayStoreReview(Godot godot) {
        super(godot);

        this.review_manager = ReviewManagerFactory.create(getActivity());
    }

    private static final int STATUS_SUCCESS = 0;
    private static final int REVIEW_MANAGER_CREATION_FAILED = 1;
    private static final int REVIEW_TASK_CREATION_FAILED = 2;


    @NonNull
    @Override
    public String getPluginName() {
        return "GodotPlayStoreReview";
    }

    private void emit_finished(int status_code) {
        emitSignal("finished", status_code);
    }

    public void start_review() {
        if (review_manager == null) {
            emit_finished(REVIEW_MANAGER_CREATION_FAILED);
        }
        Task<ReviewInfo> request = review_manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (! task.isSuccessful()) {
                emit_finished(REVIEW_TASK_CREATION_FAILED);
            }
            else {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = review_manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(flow_task -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    emit_finished(STATUS_SUCCESS);
                });
            }
        });
    }

    @NonNull
    @Override
    public List<String> getPluginMethods() {
        return Arrays.asList("start_review");
    }

    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();

        signals.add(new SignalInfo("finished", Integer.class));

        return signals;
    }

    // Forwarded callbacks you can reimplement, as SDKs often need them.

    public void onMainActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public void onMainPause() {
    }

    public void onMainResume() {
    }

    public void onMainDestroy() {
    }

    public void onGLDrawFrame(GL10 gl) {
    }

    public void onGLSurfaceChanged(GL10 gl, int width, int height) {
    } // Singletons will always miss first 'onGLSurfaceChanged' call.

}
