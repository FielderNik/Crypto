package com.example.crypto.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.crypto.MainActivity
import com.example.crypto.R

/**
 * Implementation of App Widget functionality.
 */
class WidgetCrypto : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        /*appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.widget_crypto
            ).apply {
                setOnClickPendingIntent(R.id.btnShowProfit, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }*/

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)

/*            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java).let {
                intent -> PendingIntent.getActivity(context, 0, intent, 0)

            }

            val view = RemoteViews(context.packageName, R.layout.widget_crypto)
            view.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, view)*/


        }
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }


}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_crypto)
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    val sett = context.applicationContext.getSharedPreferences(
        MainActivity.APP_PREFERENCES,
        Context.MODE_PRIVATE
    )
    val rubPerHour: String? = sett.getString(MainActivity.APP_PREFERENCES_RUBHOUR, "")
    val usdPerHour: String? = sett.getString(MainActivity.APP_PREFERENCES_USDHOUR, "")
    val ethPerHour: String? = sett.getString(MainActivity.APP_PREFERENCES_ETHHOUR, "")

    views.setTextViewText(R.id.tvWidgetRUBPerHour, rubPerHour)
    views.setTextViewText(R.id.tvWidgetUSDPerHour, usdPerHour)
    views.setTextViewText(R.id.tvWidgetETHPerHour, ethPerHour)


    val intentUpdate = Intent(context, WidgetCrypto::class.java)
    intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)

    val idArray = intArrayOf(appWidgetId)
    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

    val pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.tvWidgetRUBPerHour, pendingUpdate)
    views.setOnClickPendingIntent(R.id.tvWidgetUSDPerHour, pendingUpdate)
    views.setOnClickPendingIntent(R.id.tvWidgetETHPerHour, pendingUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}



