<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">

    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>Update Hardware Inventory</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/whChange/updateChange" method="post">
                            <input type="hidden" name="refId" value="${whInventory.refId}" />
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">Box No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="boxNo" name="boxNo" value="${whInventory.boxNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="gtsNo" class="col-lg-3 control-label">GTS No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="gtsNo" name="gtsNo" value="${whInventory.gtsNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-3 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whInventory.materialPassNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-3 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whInventory.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-3 control-label">Hardware Category</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whInventory.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-3 control-label">Hardware ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whInventory.equipmentId}" readonly>
                                </div>
                            </div>
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-3 control-label">Quantity</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whInventory.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryRack" class="col-lg-3 control-label">Inventory Rack</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryRack" name="inventoryRack" value="${whInventory.inventoryRack}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryShelf" class="col-lg-3 control-label">Inventory Shelf</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryShelf" name="inventoryShelf" value="${whInventory.inventoryShelf}" readonly>
                                </div>
                            </div>
                            <div id = "alert_placeholder2"></div>
                            <a href="${contextPath}/whChange/${whInventory.equipmentType}" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
                            <div class="pull-right">
                                <!--<button type="reset" class="btn btn-secondary cancel">Reset</button>-->
                                <button type="submit" id="printBarcode" name="printBarcode" class="btn btn-primary">Print Barcode Sticker</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {

                var element = $('#equipmentType');
                if (element.val() === "Motherboard") {
                    $("#quantitydiv").hide();
                } else if (element.val() === "Stencil") {
                    $("#quantitydiv").hide();
                } else if (element.val() === "Tray") {
                    $("#quantitydiv").show();
                } else if (element.val() === "PCB") {
                    $("#quantitydiv").show();
                } else {
                    $("#quantitydiv").hide();
                }

            });

            $('#printBarcode').on('click', function () {
                window.open('${contextPath}/whChange/viewWhBarcodeStickerPdf/${whInventory.refId}', 'Barcode Sticker', 'width=1300,height=960').print();

            });
        </script>
    </s:layout-component>
</s:layout-render>