package com.site.vs.videostation.kit.conversation.message.viewholder;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import com.site.vs.videostation.Config;
import com.site.vs.videostation.kit.annotation.EnableContextMenu;
import com.site.vs.videostation.kit.annotation.MessageContentType;
import com.site.vs.videostation.kit.annotation.ReceiveLayoutRes;
import com.site.vs.videostation.kit.annotation.SendLayoutRes;
import com.site.vs.videostation.kit.conversation.message.model.UiMessage;
import com.site.vs.videostation.kit.third.utils.UIUtils;
import com.site.vs.videostation.R;
import cn.wildfirechat.message.SoundMessageContent;
import cn.wildfirechat.message.core.MessageDirection;

@MessageContentType(SoundMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_audio_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_audio_receive)
@EnableContextMenu
public class AudioMessageContentViewHolder extends MediaMessageContentViewHolder {
    @BindView(R.id.audioImageView)
    ImageView ivAudio;
    @BindView(R.id.durationTextView)
    TextView durationTextView;
    @BindView(R.id.audioContentLayout)
    RelativeLayout contentLayout;

    public AudioMessageContentViewHolder(FragmentActivity context, RecyclerView.Adapter adapter, View itemView) {
        super(context, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        super.onBind(message);
        SoundMessageContent voiceMessage = (SoundMessageContent) message.message.content;
        int increment = UIUtils.getDisplayWidth() / 2 / Config.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * voiceMessage.getDuration();

        durationTextView.setText(voiceMessage.getDuration() + "''");
        ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
        params.width = UIUtils.dip2Px(65) + UIUtils.dip2Px(increment);
        contentLayout.setLayoutParams(params);

        AnimationDrawable animation;
        if (message.isPlaying) {
            animation = (AnimationDrawable) ivAudio.getBackground();
            if (!animation.isRunning()) {
                animation.start();
            }
        } else {
            // TODO 不知道怎么回事，动画开始了，就停不下来, 所以采用这种方式
            ivAudio.setBackground(null);
            if (message.message.direction == MessageDirection.Send) {
                ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
            } else {
                ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
            }
        }

        // 下载完成，开始播放
        if (message.progress == 100) {
            message.progress = 0;
            itemView.post(() -> {
                conversationViewModel.playAudioMessage(message);
            });
        }
    }

    @Override
    public void onViewRecycled() {
        // TODO 可实现语音是否持续播放、中断登录逻辑
    }

    @OnClick(R.id.audioContentLayout)
    public void onClick(View view) {
        File file = conversationViewModel.mediaMessageContentFile(message);
        if (file == null) {
            return;
        }
        if (file.exists()) {
            conversationViewModel.playAudioMessage(message);
        } else {
            if (message.isDownloading) {
                return;
            }
            conversationViewModel.downloadMedia(message, file);
        }
    }

}
