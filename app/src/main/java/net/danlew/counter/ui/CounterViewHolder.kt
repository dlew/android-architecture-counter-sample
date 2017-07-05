package net.danlew.counter.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import net.danlew.counter.R
import net.danlew.counter.data.Counter
import net.danlew.counter.data.CounterChange

class CounterViewHolder(context: Context, parent: ViewGroup)
  : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.counter, parent, false)) {

  companion object {
    private val DUMMY_COUNTER = Counter(-1)
  }

  @BindView(R.id.counter_name) lateinit var counterName: EditText
  @BindView(R.id.count) lateinit var count: TextView
  @BindView(R.id.plus_button) lateinit var plusButton: Button
  @BindView(R.id.minus_button) lateinit var minusButton: Button

  private var counter: Counter = DUMMY_COUNTER
  private lateinit var listener: Listener

  init {
    ButterKnife.bind(this, itemView)

    plusButton.setOnClickListener {
      listener.onCounterChange(CounterChange.Count(counter.id, 1))
    }

    minusButton.setOnClickListener {
      listener.onCounterChange(CounterChange.Count(counter.id, -1))
    }

    counterName.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        // If you try to clear focus immediately it doesn't work
        v.postDelayed({ v.clearFocus() }, 0)
      }
      false
    }

    counterName.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable)
          = listener.onCounterChange(CounterChange.Name(counter.id, s.toString()))

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
  }

  fun bind(counter: Counter, listener: Listener) {
    if (this.counter == counter) {
      return
    }

    this.listener = listener
    this.counter = counter
    if (counterName.text.toString() != counter.name) {
      counterName.setText(counter.name)
    }
    count.text = counter.count.toString()
  }

  fun detach() {
    if (counterName.hasFocus()) {
      counterName.clearFocus()
    }
  }

  interface Listener {
    fun onCounterChange(counterChange: CounterChange)
  }
}