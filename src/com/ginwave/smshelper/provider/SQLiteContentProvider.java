package com.ginwave.smshelper.provider;

import java.util.ArrayList;

import com.ginwave.smshelper.database.DatabaseHelper;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLiteContentProvider extends ContentProvider{

    protected DatabaseHelper mOpenHelper;
    private volatile boolean mNotifyChange;
    protected SQLiteDatabase mDb;

    private final ThreadLocal<Boolean> mApplyingBatch = new ThreadLocal<Boolean>();
    private static final int SLEEP_AFTER_YIELD_DELAY = 4000;
    private static final int MAX_OPERATIONS_PER_YIELD_POINT = 500;

    protected abstract Uri insertInTransaction(Uri uri, ContentValues values);

    protected abstract int updateInTransaction(Uri uri, ContentValues values, String selection,
            String[] selectionArgs);
    protected abstract int deleteInTransaction(Uri uri, String selection, String[] selectionArgs);
    protected abstract void notifyChange();

    protected DatabaseHelper getDatabaseHelper() {
        return mOpenHelper;
    }

    private boolean applyingBatch() {
        return mApplyingBatch.get() != null && mApplyingBatch.get();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            try {
                result = insertInTransaction(uri, values);
                if (result != null) {
                    mNotifyChange = true;
                }
            } finally {
            }
            onEndTransaction();
        } else {
            result = insertInTransaction(uri, values);
            if (result != null) {
                mNotifyChange = true;
            }
        }
        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = values.length;
        mDb = mOpenHelper.getWritableDatabase();
        try {
            for (int i = 0; i < numValues; i++) {
                Uri result = insertInTransaction(uri, values[i]);
                if (result != null) {
                    mNotifyChange = true;
                }
                mDb.yieldIfContendedSafely();
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

        onEndTransaction();
        return numValues;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            try {
                count = updateInTransaction(uri, values, selection, selectionArgs);
                if (count > 0) {
                    mNotifyChange = true;
                }
            } finally {
            }

            onEndTransaction();
        } else {
            count = updateInTransaction(uri, values, selection, selectionArgs);
            if (count > 0) {
                mNotifyChange = true;
            }
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        boolean applyingBatch = applyingBatch();
        if (!applyingBatch) {
            mDb = mOpenHelper.getWritableDatabase();
            try {
                count = deleteInTransaction(uri, selection, selectionArgs);
                if (count > 0) {
                    mNotifyChange = true;
                }
            } finally {
            }

            onEndTransaction();
        } else {
            count = deleteInTransaction(uri, selection, selectionArgs);
            if (count > 0) {
                mNotifyChange = true;
            }
        }
        return count;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        int ypCount = 0;
        int opCount = 0;
        mDb = mOpenHelper.getWritableDatabase();
        try {
            mApplyingBatch.set(true);
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                if (++opCount >= MAX_OPERATIONS_PER_YIELD_POINT) {
                    throw new OperationApplicationException(
                            "Too many content provider operations between yield points. "
                                    + "The maximum number of operations per yield point is "
                                    + MAX_OPERATIONS_PER_YIELD_POINT, ypCount);
                }
                final ContentProviderOperation operation = operations.get(i);
                if (i > 0 && operation.isYieldAllowed()) {
                    opCount = 0;
                    if (mDb.yieldIfContendedSafely(SLEEP_AFTER_YIELD_DELAY)) {
                        ypCount++;
                    }
                }
                results[i] = operation.apply(this, results, i);
            }
            mDb.setTransactionSuccessful();
            return results;
        } finally {
            mApplyingBatch.set(false);
            mDb.endTransaction();
            onEndTransaction();
        }
    }



    protected void onEndTransaction() {
        if (mNotifyChange) {
            mNotifyChange = false;
            notifyChange();
        }
    }
    
    @Override
    public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
    {
    	
      this.mDb = mOpenHelper.getReadableDatabase();
      Cursor localCursor = queryIn(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
      if (localCursor != null)
        notifyChange();
      return localCursor;
    }

    protected abstract Cursor queryIn(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2);
}
