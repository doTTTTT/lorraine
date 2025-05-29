package screens.main

import DocsPage

internal sealed interface MainAction {

    data class SelectPage(val menu: DocsPage) : MainAction

}