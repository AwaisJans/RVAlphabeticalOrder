package com.jans.rv.sample.ordered.app.libAlphabeticalScroller

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R
import kotlin.math.max
import kotlin.math.min

class RecyclerViewFastScroller : LinearLayout, AlphabetAdapter.OnItemClickListener,
    OnTouchListener {
    private var recyclerView: RecyclerView? = null
    private var alphabets: List<AlphabetItem>? = null
    private var alphabetRecyclerView: RecyclerView? = null
    private var alphabetAdapter: AlphabetAdapter? = null
    private var isInitialized = false
    private var height = 0

    interface BubbleTextGetter {
        fun getTextToShowInBubble(pos: Int): String
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialiseView(context)
    }

    constructor(context: Context?) : super(context) {
        initialiseView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialiseView(context)
    }

    protected fun initialiseView(context: Context?) {
        if (isInitialized) {
            return
        }

        // Init linear layout
        isInitialized = true
        orientation = HORIZONTAL
        clipChildren = false
        inflate(context, R.layout.fast_scroller, this)

        // Init alphabet recycler view
        alphabetRecyclerView = findViewById<View>(R.id.alphabet) as RecyclerView
        alphabetRecyclerView!!.layoutManager = LinearLayoutManager(getContext())
        alphabetRecyclerView!!.setOnTouchListener(this)
    }

    //----------------------------------------------------------------------------------------------
    //  Linear layout events
    //----------------------------------------------------------------------------------------------
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        height = h
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                setRecyclerViewPosition(y)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setRecyclerViewPosition(y: Float) {
        if (recyclerView != null) {
            val itemCount = recyclerView!!.adapter!!.itemCount
            val proportion = y / height.toFloat()
            val targetPos =
                getValueInRange(0, itemCount - 1, (proportion * itemCount.toFloat()).toInt())
            (recyclerView!!.layoutManager as LinearLayoutManager?)!!.scrollToPositionWithOffset(
                targetPos,
                0
            )

            val bubbleText =
                (recyclerView!!.adapter as BubbleTextGetter?)!!.getTextToShowInBubble(targetPos)
            setAlphabetWordSelected(bubbleText)
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Implement events
    //----------------------------------------------------------------------------------------------
    override fun OnItemClicked(alphabetPosition: Int, position: Int) {
        performSelectedAlphabetWord(position)
        takeRecyclerViewScrollToAlphabetPosition(alphabetPosition)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val rect = Rect()
                val childCount = alphabetRecyclerView!!.childCount
                val listViewCoords = IntArray(2)
                alphabetRecyclerView!!.getLocationOnScreen(listViewCoords)
                val x = motionEvent.rawX.toInt() - listViewCoords[0]
                val y = motionEvent.rawY.toInt() - listViewCoords[1]

                var child: View
                var i = 0
                while (i < childCount) {
                    child = alphabetRecyclerView!!.getChildAt(i)
                    child.getHitRect(rect)

                    // This is your pressed view
                    if (rect.contains(x, y)) {
                        val layoutManager =
                            (alphabetRecyclerView!!.layoutManager as LinearLayoutManager?)
                        val firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
                        val position = i + firstVisiblePosition
                        performSelectedAlphabetWord(position)
                        alphabetTouchEventOnItem(position)
                        break
                    }
                    i++
                }
                view.onTouchEvent(motionEvent)
            }
        }
        return true
    }

    //----------------------------------------------------------------------------------------------
    //  Alphabet Section
    //----------------------------------------------------------------------------------------------
    fun setRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        return
                    }
                    val verticalScrollOffset = recyclerView.computeVerticalScrollOffset()
                    val verticalScrollRange = recyclerView.computeVerticalScrollRange()
                    val proportion =
                        verticalScrollOffset.toFloat() / (verticalScrollRange.toFloat() - height)
                    setRecyclerViewPositionWithoutScrolling(height * proportion)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    }
                }
            }
        this.recyclerView!!.addOnScrollListener(onScrollListener)
    }

    fun setUpAlphabet(alphabetItems: List<AlphabetItem>?) {
        if (alphabetItems == null || alphabetItems.size <= 0) return

        alphabets = alphabetItems
        alphabetAdapter = AlphabetAdapter(context, alphabets)
        alphabetAdapter!!.setOnItemClickListener(this)
        alphabetRecyclerView!!.adapter = alphabetAdapter
    }

    private fun setRecyclerViewPositionWithoutScrolling(y: Float) {
        if (recyclerView != null) {
            val itemCount = recyclerView!!.adapter!!.itemCount
            val proportion = y / height.toFloat()
            val targetPos =
                getValueInRange(0, itemCount - 1, (proportion * itemCount.toFloat()).toInt())
            val bubbleText =
                (recyclerView!!.adapter as BubbleTextGetter?)!!.getTextToShowInBubble(targetPos)
            setAlphabetWordSelected(bubbleText)
        }
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = max(min.toDouble(), value.toDouble()).toInt()
        return min(minimum.toDouble(), max.toDouble()).toInt()
    }

    private fun performSelectedAlphabetWord(position: Int) {
        if (position < 0 || position >= alphabets!!.size) {
            return
        }

        for (alphabetItem in alphabets!!) {
            alphabetItem.isActive = false
        }

        alphabets!![position].isActive = true
        alphabetAdapter!!.refreshDataChange(alphabets)
    }

    private fun alphabetTouchEventOnItem(position: Int) {
        if (alphabets == null || position < 0 || position >= alphabets!!.size) {
            return
        }

        takeRecyclerViewScrollToAlphabetPosition(alphabets!![position].position)
    }

    private fun takeRecyclerViewScrollToAlphabetPosition(position: Int) {
        if (recyclerView == null || recyclerView!!.adapter == null) {
            return
        }

        val count = recyclerView!!.adapter!!.itemCount
        if (position < 0 || position > count) {
            return
        }

        (recyclerView!!.layoutManager as LinearLayoutManager?)!!.scrollToPositionWithOffset(
            position,
            0
        )
    }

    private fun setAlphabetWordSelected(bubbleText: String?) {
        if (bubbleText == null || bubbleText.trim { it <= ' ' }.isEmpty()) {
            return
        }

        for (i in alphabets!!.indices) {
            val alphabetItem = alphabets!![i]
            if (alphabetItem == null || alphabetItem.word.trim { it <= ' ' }.isEmpty()) {
                continue
            }

            if (alphabetItem.word == bubbleText) {
                performSelectedAlphabetWord(i)
                alphabetRecyclerView!!.smoothScrollToPosition(i)
                break
            }
        }
    }
}