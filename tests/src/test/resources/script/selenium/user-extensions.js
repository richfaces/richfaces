
IEBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
    var win = this.getCurrentWindow();
    triggerEvent(element, 'focus', false);

    var wasChecked = element.checked;

    // Set a flag that records if the page will unload - this isn't always accurate, because
    // <a href="javascript:alert('foo'):"> triggers the onbeforeunload event, even thought the page won't unload
    var pageUnloading = false;
    var pageUnloadDetector = function() {
        pageUnloading = true;
    };
    win.attachEvent("onbeforeunload", pageUnloadDetector);
    this._modifyElementTarget(element);
    if (element[eventType] && !this.controlKeyDown && !this.altKeyDown && !this.shiftKeyDown && !this.metaKeyDown) {
        element[eventType]();
    }
    else {
        this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);
    }


    // If the page is going to unload - still attempt to fire any subsequent events.
    // However, we can't guarantee that the page won't unload half way through, so we need to handle exceptions.
    try {
        win.detachEvent("onbeforeunload", pageUnloadDetector);

        if (this._windowClosed(win)) {
            return;
        }

        // Onchange event is not triggered automatically in IE.
        if (isDefined(element.checked) && wasChecked != element.checked) {
            triggerEvent(element, 'change', true);
        }

    }
    catch (e) {
        // If the page is unloading, we may get a "Permission denied" or "Unspecified error".
        // Just ignore it, because the document may have unloaded.
        if (pageUnloading) {
            LOG.logHook = function() {
            };
            LOG.warn("Caught exception when firing events on unloading page: " + e.message);
            return;
        }
        throw e;
    }
};



Selenium.prototype.doDragAndDropToObject = function(locatorOfObjectToBeDragged, locatorOfDragDestinationObject) {
/** Drags an element and drops it on another element
   *
   * @param locatorOfObjectToBeDragged an element to be dragged
   * @param locatorOfDragDestinationObject an element whose location (i.e., whose center-most pixel) will be the point where locatorOfObjectToBeDragged  is dropped
   */
   var startX = this.getElementPositionLeft(locatorOfObjectToBeDragged);
   var startY = this.getElementPositionTop(locatorOfObjectToBeDragged);
   
   var destinationLeftX = this.getElementPositionLeft(locatorOfDragDestinationObject);
   var destinationTopY = this.getElementPositionTop(locatorOfDragDestinationObject);
   var destinationWidth = this.getElementWidth(locatorOfDragDestinationObject);
   var destinationHeight = this.getElementHeight(locatorOfDragDestinationObject);

   var endX = Math.round(destinationLeftX + (destinationWidth / 2));
   var endY = Math.round(destinationTopY + (destinationHeight / 2));
   
   var deltaX = endX - startX;
   var deltaY = endY - startY;
   
   var movementsString = "" + deltaX + "," + deltaY;
   
   this.doDragAndDrop(locatorOfObjectToBeDragged, movementsString);
    var objectToBeDragged = this.browserbot.findElement(locatorOfObjectToBeDragged); 
    var clientStartXY = getClientXY(objectToBeDragged)
    var clientStartX = clientStartXY[0];
    var clientStartY = clientStartXY[1];
    
    var movements = movementsString.split(/,/);
    var movementX = Number(movements[0]);
    var movementY = Number(movements[1]);
    
    var clientFinishX = ((clientStartX + movementX) < 0) ? 0 : (clientStartX + movementX);
    var clientFinishY = ((clientStartY + movementY) < 0) ? 0 : (clientStartY + movementY);
   
   // We should also fire event on DragDestinationObject. 
   var dragDestinationObject = this.browserbot.findElement(locatorOfDragDestinationObject);
   this.browserbot.triggerMouseEvent(dragDestinationObject, 'mouseover',   true, clientFinishX, clientFinishY);
      
   this.browserbot.triggerMouseEvent(objectToBeDragged, 'mousemove',   true, clientFinishX, clientFinishY);
   this.browserbot.triggerMouseEvent(objectToBeDragged, 'mouseup',   true, clientFinishX, clientFinishY);
   this.browserbot.triggerMouseEvent(dragDestinationObject, 'mouseup',   true, clientFinishX, clientFinishY);
   
};


Selenium.prototype.doDragAndDrop = function(locator, movementsString) {
    /** Drags an element a certain distance and then drops it
    * @param locator an element locator
    * @param movementsString offset in pixels from the current location to which the element should be moved, e.g., "+70,-300"
    */
    var element = this.browserbot.findElement(locator);
    var clientStartXY = getClientXY(element)
    var clientStartX = clientStartXY[0];
    var clientStartY = clientStartXY[1];
   
    var movements = movementsString.split(/,/);
    var movementX = Number(movements[0]);
    var movementY = Number(movements[1]);
    
    var clientFinishX = ((clientStartX + movementX) < 0) ? 0 : (clientStartX + movementX);
    var clientFinishY = ((clientStartY + movementY) < 0) ? 0 : (clientStartY + movementY);
    
    var mouseSpeed = this.mouseSpeed;
    var move = function(current, dest) {
        if (current == dest) return current;
        if (Math.abs(current - dest) < mouseSpeed) return dest;
        return (current < dest) ? current + mouseSpeed : current - mouseSpeed;
    }
    
    this.browserbot.triggerMouseEvent(element, 'mousedown', true, clientStartX, clientStartY);
    this.browserbot.triggerMouseEvent(element, 'mousemove',   true, clientStartX, clientStartY);
    var clientX = clientStartX;
    var clientY = clientStartY;
    
    while ((clientX != clientFinishX) || (clientY != clientFinishY)) {
        clientX = move(clientX, clientFinishX);
        clientY = move(clientY, clientFinishY);
        this.browserbot.triggerMouseEvent(element, 'mousemove', true, clientX, clientY);
    }
    
};

try {
if (commandFactory && selenium) {
	commandFactory.registerAll(selenium);
}
}catch(e){}




