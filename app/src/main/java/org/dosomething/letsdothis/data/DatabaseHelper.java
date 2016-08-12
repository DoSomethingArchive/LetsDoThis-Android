package org.dosomething.letsdothis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import co.touchlab.android.threading.tasks.TaskQueue;


/**
 * Provides convenience methods for our local database.
 * @author toidiu
 */
// TODO Refactor out?  Looks like Campaign, CampaignActions are not used; user might be better in private shared prefs
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String DATABASE_FILE_NAME = "justdoit";
    private static final int    VERSION            = 1;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private static DatabaseHelper instance;
    // @reminder Ordering matters, create foreign key dependant classes later
    private final Class[] tableClasses = new Class[] {User.class, CampaignActions.class, Campaign.class};

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_FILE_NAME, null, VERSION);
    }

    public static TaskQueue defaultDatabaseQueue(Context context)
    {
        return TaskQueue.loadQueue(context, "database");
    }

    @NotNull
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            for(Class mTableClass : tableClasses)
            {
                TableUtils.createTable(connectionSource, mTableClass);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        for(int i = tableClasses.length - 1; i >= 0; i--)
        {
            Class tableClass = tableClasses[i];
            try
            {
                TableUtils.dropTable(connectionSource, tableClass, true);
            }
            catch(SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        onCreate(database, connectionSource);
    }

    public void runInTransaction(Runnable r) throws SQLException
    {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try
        {
            r.run();
            writableDatabase.setTransactionSuccessful();
        }
        finally
        {
            writableDatabase.endTransaction();
        }
    }

    public Dao<User, String> getUserDao() throws SQLException
    {
        return getDao(User.class);
    }

    public Dao<Campaign, Integer> getCampDao() throws SQLException
    {
        return getDao(Campaign.class);
    }



}


