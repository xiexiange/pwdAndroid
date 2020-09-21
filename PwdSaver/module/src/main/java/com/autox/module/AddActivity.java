package com.autox.module;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.autox.base.PrefUtil;
import com.autox.module.Constant.CATEGORY_TYPE;
import com.autox.module.entities.DbChanged;
import com.autox.module.entities.EventGoMainPage;
import com.autox.module.localdata.database.DbHelper;
import com.autox.module.localdata.database.items.PwdItem;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.module.util.ClientEncodeUtil;
import com.autox.module.util.MaskUtil;
import com.autox.module.util.ModuleBaseUtil;
import com.autox.pwd_module.R;
import com.autox.views.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

public class AddActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_ITEM = "pwd_item";
    private static final int REQUEST_CODE_SHOW_CLEAR_ACCOUNT = 1000;
    private static final int REQUEST_CODE_PLATFORM_LIST = 1001;
    private CATEGORY_TYPE mType;
    private ImageView mIconIV;
    private TextView mTitleTV;
    private EditText mAccountET;
    private ImageView mAccountCloseIV;
    private TextView mPlatformTV;
    private EditText mPwdET;
    private EditText mNoteET;
    private ImageView mPwdCloseIV;
    private TextView mShowClearAccountTv;
    private ImageView mShareAccountIv;
    private ImageView mCopyAccountIv;
    private PwdItem mPwdItem;
    private TextView mLikeHeart;
    String mTitle = "其它";
    private RelativeLayout mBackRL;
    private TextView mSaveTV;
    private ConstraintLayout mPlatformWrapper;
    private ImageView mPlatformImage;

    private boolean mLikeState = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_add);
        mType = (CATEGORY_TYPE) getIntent().getSerializableExtra(EXTRA_TYPE);
        mPwdItem = (PwdItem) getIntent().getSerializableExtra(EXTRA_ITEM);
        initViews();
        bindEvents();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void start(Context context, CATEGORY_TYPE type, PwdItem item) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_ITEM, item);
        context.startActivity(intent);
    }

    public static void start(Context context, PwdItem item) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        context.startActivity(intent);
    }

    private void initViews() {
        mIconIV = findViewById(R.id.add_page_detail_icon);
        mTitleTV = findViewById(R.id.add_page_detail_title);
        mBackRL = findViewById(R.id.add_page_back_wrapper);
        mSaveTV = findViewById(R.id.add_page_save_btn);
        mPlatformTV = findViewById(R.id.add_page_platform_content);
        mAccountET = findViewById(R.id.add_page_account_content);
        mAccountCloseIV = findViewById(R.id.add_page_clear_account);
        mPwdET = findViewById(R.id.add_page_pwd_content);
        mNoteET = findViewById(R.id.add_page_note_edit);
        mPwdCloseIV = findViewById(R.id.add_page_clear_pwd);
        mPlatformWrapper = findViewById(R.id.add_page_platform_wrapper);
        mPlatformImage = findViewById(R.id.add_page_platform_image);
        mShowClearAccountTv = findViewById(R.id.show_clear_account);
        mShareAccountIv = findViewById(R.id.account_share);
        mCopyAccountIv = findViewById(R.id.account_copy);
        mLikeHeart = findViewById(R.id.icon_like_heart);
        int imageId;
        String platName = "";
        int drawable = R.drawable.platform_icon_other;
        if (mType != null) {
            switch (mType) {
                case WORK:
                    mTitle = "工作";
                    imageId = R.drawable.icon_work;
                    platName = "邮箱";
                    drawable = R.drawable.platform_icon_mail;
                    break;
                case VIDEO:
                    mTitle = "视频";
                    imageId = R.drawable.icon_video;
                    drawable = R.drawable.platform_icon_iqiyi;
                    platName = "爱奇艺视频";
                    break;
                case MAIL:
                    mTitle = "邮箱";
                    imageId = R.drawable.icon_mail;
                    drawable = R.drawable.platform_icon_qqmail;
                    platName = "QQ邮箱";
                    break;
                case MONEY:
                    mTitle = "金融";
                    imageId = R.drawable.icon_wallet;
                    drawable = R.drawable.platform_icon_zhaoshang;
                    platName = "招商银行(暂不支持密码)";
                    mPwdET.setEnabled(false);
                    mPwdET.setText("暂不支持");
                    break;
                case GAME:
                    mTitle = "游戏";
                    imageId = R.drawable.icon_game;
                    drawable = R.drawable.platform_icon_mobilegame;
                    platName = "手机游戏";
                    break;
                case WEB:
                    mTitle = "网址";
                    imageId = R.drawable.icon_web;
                    drawable = R.drawable.platform_icon_google;
                    platName = "谷歌";
                    break;
                case OTHER:
                default:
                    mTitle = "其它";
                    imageId = R.drawable.icon_other;
                    platName = "其它";
                    break;
            }
            mIconIV.setImageResource(imageId);

        }
        if (mPwdItem != null) {
            String account = mPwdItem.account();
            if (PrefUtil.getBoolean(SharedPrefKeys.KEY_ENABLE_ACCOUNT_MASK, false)) {
                account = MaskUtil.mask(mPwdItem.account());
                if (account.contains("*")) {
                    mShowClearAccountTv.setVisibility(View.VISIBLE);
                    tryShowShareBtn(false);
                    mCopyAccountIv.setVisibility(View.GONE);
                } else {
                    tryShowShareBtn(true);
                    mCopyAccountIv.setVisibility(View.VISIBLE);
                }
            } else {
                tryShowShareBtn(true);
                mCopyAccountIv.setVisibility(View.VISIBLE);
            }
            mAccountET.setText(account);
            mPlatformTV.setText(mPwdItem.platform());
            mPlatformImage.setImageResource(PlatformListActivity.getDrawableIdByName(mPwdItem.platform()));
            mAccountET.setEnabled(false);
            try {
                mPwdET.setText(ClientEncodeUtil.decode(mPwdItem.pwd()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mNoteET.setText(mPwdItem.note());
            findViewById(R.id.add_page_platform_arrow_right).setVisibility(View.INVISIBLE);
            mLikeState = mPwdItem.favor() == 1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                int likeDrawable = (mLikeState ? R.drawable.icon_like_heart : R.drawable.icon_dislike_heart);
                mLikeHeart.setBackgroundResource(likeDrawable);
            }
        } else {
            mPlatformTV.setText(platName);
            mPlatformImage.setImageResource(drawable);
        }
        mTitleTV.setText(mTitle);
        if (mType == null) {
            findViewById(R.id.add_page_title_wrapper).setVisibility(View.GONE);
        }

    }

    private void bindEvents() {
        mBackRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSaveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = mTitle;
                String platform = mPlatformTV.getText().toString();
                String account = mAccountET.getText().toString().replace("\"", "“");
                String pwd = mPwdET.getText().toString().replace("\"", "“");
                String note = mNoteET.getText().toString().replace("\"", "“");
                if (TextUtils.isEmpty(platform)) {
                    Toast.makeText(AddActivity.this, "平台名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(AddActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    mAccountET.requestFocus();
                    showKeyboard(v);
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(AddActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    mPwdET.requestFocus();
                    showKeyboard(v);
                    return;
                }
                if (note == null) {
                    note = "";
                }
                String pwdmask = ClientEncodeUtil.encode(pwd);
                DbHelper.getInstance().insert(new PwdItem(type, platform, account, pwdmask, System.currentTimeMillis(), note, mLikeState ? 1 : 0), false);
                Toast.makeText(AddActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                if (mPwdItem == null) {
                    ModuleBaseUtil.recordUsage(Constant.USAGE_SAVE_CLICK_ADD, "");
                } else {
                    ModuleBaseUtil.recordUsage(Constant.USAGE_CHECK_SAVE_CLICK, "");
                }
                EventBus.getDefault().post(new DbChanged());
                EventBus.getDefault().post(new EventGoMainPage());
                ModuleBaseUtil.recordUsage(Constant.USAGE_PLATFORM, platform);
                finish();
            }
        });
        mAccountCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountET.setText("");
                mAccountET.requestFocus();
                showKeyboard(v);
            }
        });

        InputFilter contentFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return !(c >=7 && c <= 32);
            }
        };
        mAccountET.setFilters(new InputFilter[]{contentFilter});
        mAccountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable edt) {
                int length = mAccountET.getText().toString().length();
                if (length == 0 || !mAccountET.isEnabled()) {
                    mAccountCloseIV.setVisibility(View.GONE);
                } else {
                    mAccountCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });
        mPwdCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdET.setText("");
                mPwdET.requestFocus();
                showKeyboard(v);

            }
        });

        InputFilter pwdFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return c >= 33 && c <= 122;
            }
        };

        mPwdET.setFilters(new InputFilter[]{pwdFilter});

        mPwdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                //https://blog.csdn.net/qq_30054961/article/details/82463748
//                if(edt.toString().getBytes().length != edt.length()){
//                    edt.delete(temp.length()-1, temp.length());
//                }
//                try {
//                    temp = edt.toString();
//                    String tem = temp.substring(temp.length()-1, temp.length());
//                    char[] temC = tem.toCharArray();
//                    int mid = temC[0];
//                    boolean needDelete = true;
//                    if(mid >= 33 && mid <= 122){
//                        needDelete = false;
//                    }
//                    if (needDelete) {
//                        edt.clear();
//                    }
//
//                } catch (Exception e) {
//                }


                int length = mPwdET.getText().toString().length();
                if (length == 0) {
                    mPwdCloseIV.setVisibility(View.GONE);
                } else {
                    mPwdCloseIV.setVisibility(View.VISIBLE);
                }
            }
        });

        mPlatformWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPwdItem != null) {
                    return;
                }
                Intent intent = new Intent(AddActivity.this, PlatformListActivity.class);
                intent.putExtra(EXTRA_TYPE, mType);
                startActivityForResult(intent, REQUEST_CODE_PLATFORM_LIST);
            }
        });

        mShowClearAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, PwdVerifyActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SHOW_CLEAR_ACCOUNT);
            }
        });
        mShareAccountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mAccountET.getText().toString());
                shareIntent = Intent.createChooser(shareIntent, "账号分享");
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
                ModuleBaseUtil.recordUsage(Constant.USAGE_SHARE_CLICK, "1");
            }
        });

        mCopyAccountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", mAccountET.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(AddActivity.this, "账号已经复制到剪切板中", Toast.LENGTH_SHORT).show();
                ModuleBaseUtil.recordUsage(Constant.USAGE_COPY_CLICK, "1");
            }
        });

        mLikeHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLikeState) {
                    mLikeState = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mLikeHeart.setBackgroundResource(R.drawable.icon_like_heart);
                        if (mPwdItem != null) {
                            DbHelper.getInstance().like(mPwdItem, true);
                            EventBus.getDefault().post(new DbChanged());
                        }
                    }
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this)
                        .setTitle("取消收藏")
                        .setMessage("确认将该条信息移出\"收藏\"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mLikeState = false;
                                mLikeHeart.setBackgroundResource(R.drawable.icon_dislike_heart);
                                if (mPwdItem != null) {
                                    DbHelper.getInstance().like(mPwdItem, false);
                                    EventBus.getDefault().post(new DbChanged());
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });

    }

    private void tryShowShareBtn(boolean show) {
        if (!show) {
            mShareAccountIv.setVisibility(View.GONE);
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Here is the Shared text.");
//切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
        shareIntent = Intent.createChooser(shareIntent, "Here is the title of Select box");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            mShareAccountIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PLATFORM_LIST:
                if (data == null) {
                    return;
                }
                String resultName = data.getStringExtra("result");
                int drawable = PlatformListActivity.getDrawableIdByName(resultName);
                mPlatformTV.setText(resultName);
                mPlatformImage.setImageResource(drawable);
            case REQUEST_CODE_SHOW_CLEAR_ACCOUNT:
                if (resultCode == PwdVerifyActivity.RESULT_OK) {
                    mAccountET.setText(mPwdItem.account());
                    mShowClearAccountTv.setVisibility(View.GONE);
                    tryShowShareBtn(true);
                    mCopyAccountIv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showKeyboard(View v) {
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

}
