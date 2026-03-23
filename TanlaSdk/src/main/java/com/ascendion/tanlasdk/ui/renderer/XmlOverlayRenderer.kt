package com.ascendion.tanlasdk.ui.renderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView



class XmlOverlayRenderer(
    private val layoutId: Int,

    // 🔥 Allow custom IDs from host app
    private val numberViewId: Int? = null,
    private val acceptButtonId: Int? = null,
    private val declineButtonId: Int? = null,
    private val reportButtonId: Int? = null

) : OverlayRenderer {

    override fun createView(
        context: Context,
        number: String,
        actions: OverlayActions
    ): View {

        val view = LayoutInflater.from(context).inflate(layoutId, null)

        // 🔥 Set phone number (if provided)
        numberViewId?.let { id ->
            view.findViewById<TextView?>(id)?.text = number
        }

        // 🔥 Bind Accept
        acceptButtonId?.let { id ->
            view.findViewById<View?>(id)?.setOnClickListener {
                actions.onAccept()
            }
        }

        // 🔥 Bind Decline
        declineButtonId?.let { id ->
            view.findViewById<View?>(id)?.setOnClickListener {
                actions.onDecline()
            }
        }

        // 🔥 Bind Report
        reportButtonId?.let { id ->
            view.findViewById<View?>(id)?.setOnClickListener {
                actions.onReport()
            }
        }

        return view
    }
}