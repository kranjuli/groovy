import com.atlassian.confluence.pages.Page
import com.atlassian.confluence.pages.PageManager
import com.atlassian.sal.api.component.ComponentLocator
import com.atlassian.confluence.spaces.Space
import com.atlassian.confluence.spaces.SpaceManager
import com.atlassian.confluence.labels.Label
import com.atlassian.confluence.labels.LabelManager

import org.apache.log4j.Logger
import org.apache.log4j.Level

// setup logging
def currentLogLevel = log.getLevel()
def scriptLogLevel = Level.INFO
log.setLevel(scriptLogLevel)

try {

    SpaceManager spaceManager = ComponentLocator.getComponent(SpaceManager)
    PageManager pageManager = ComponentLocator.getComponent(PageManager)
    LabelManager labelManager = ContainerManager.getComponent(LabelManager)
    // "stale" label
    String STALE_LABEL_VALUE = "stale"
    def staleLabel = new Label(STALE_LABEL_VALUE)
    // space of STVIT
    def space = spaceManager.getSpace('ST')
    // "archiv" main page (ID 277283121)
    Page archivMainPage = pageManager.getPage(277283121)

    // get all pages in space STVIT
    List<Page> pages = pageManager.getPages(space, true)
    // date of one year ago
    def oneYearAgoDate = ()new Date() - 365).format('yyyy-MM-dd')

    //process pages in a loop
    pages.each{ page ->
        if (page.getLastModificationDate().format('yyyy-MM-dd') < oneYearAgoDate
        && !page.getAncestors().contains(archivMainPage)
        && !page.getLabels().contains(STALE_LABEL)) {
            def currentPageLabels = page.getLabels()
            labelManager.addLabel(page, staleLabel)
        }
    }
    log.info(pages[0].getTitle())
    log.info(pages[0].getLastModificationDate())

} catch(e){
    throw(e)
} finally {
    log.setLevel(currentLogLevel)
}
