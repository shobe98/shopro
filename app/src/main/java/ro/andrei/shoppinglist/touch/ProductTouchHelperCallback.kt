package ro.andrei.shoppinglist.touch


interface ProductTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}