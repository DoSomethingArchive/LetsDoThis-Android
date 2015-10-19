package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseRbData;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class RbShareDataTask extends BaseNetworkErrorHandlerTask
{
    private final Campaign campaign;
    public        File     file;

    public RbShareDataTask(Campaign campaign)
    {
        this.campaign = campaign;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        ResponseRbData response = NetworkHelper.getNorthstarAPIService()
                .getRbData(sessionToken, campaign.id);

        ReportBack[] list = ResponseRbData.getRbList(response);
        if(list.length > 0)
        {
            ReportBack reportBack = list[list.length - 1];

            String name = "rb_" + response.data
                    .getReportback_data().id + "item_" + reportBack.id + ".jpg";
            if((getFile(name)).exists())
            {
                this.file = getFile(name);
            }
            else
            {
                this.file = downloadAndSaveTile(name, getInputStream(reportBack.getImagePath()));
            }
        }
        else
        {
            Toast.makeText(context, "Sorry, something got misplaced.", Toast.LENGTH_SHORT).show();
        }
    }

    private static File downloadAndSaveTile(String name, InputStream inputStream)
    {
        File scaledAvatar = getFile(name);

        try
        {
            FileOutputStream out = new FileOutputStream(scaledAvatar);
            IOUtils.copy(inputStream, out);
            out.close();
            inputStream.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return scaledAvatar;
    }

    @NotNull
    private static File getFile(String name)
    {
        File filesDir = new File(Environment.getExternalStorageDirectory(), "DoSomething");
        filesDir.mkdirs();
        return new File(filesDir, name);
    }

    private static InputStream getInputStream(String url) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(NetworkHelper.CONNECT_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(NetworkHelper.READ_TIMEOUT, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();

        Response response;
        InputStream inputStream = null;
        response = client.newCall(request).execute();

        if(response.code() != HttpURLConnection.HTTP_NOT_FOUND)
        {
            inputStream = response.body().byteStream();
        }

        return inputStream;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
