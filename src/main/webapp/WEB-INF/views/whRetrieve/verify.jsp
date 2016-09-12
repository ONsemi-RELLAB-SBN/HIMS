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
            <h1>Approval for Hardware Retrieve</h1>
            <div class="row">
                <div class="col-lg-6">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="approval_hardwaretrieve_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/approvalupdate" method="post">
                            <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-4 control-label">Hardware Type</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whRetrieve.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-4 control-label">Hardware ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whRetrieve.equipmentId}" readonly>
                                </div>
                            </div>  
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-4 control-label">Quantity *</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whRetrieve.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-4 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whRetrieve.materialPassNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-4 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveBy" class="col-lg-4 control-label">Requested By</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveBy" name="retrieveBy" placeholder="Retrieve For" value="${whRetrieve.requestedBy}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveDate" class="col-lg-4 control-label">Requested Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveDate" name="retrieveDate" placeholder="Retrieve For" value="${whRetrieve.requestedDate}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="remarks" class="col-lg-4 control-label">Remarks </label>
                                <div class="col-lg-8">
                                    <textarea class="form-control" rows="5" id="remarks" name="remarks" readonly>${whRetrieve.remarks}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-lg-4 control-label">Status</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="status" name="status" value="${whRetrieve.status}" readonly>
                                </div>
                            </div>
                            <a href="${contextPath}/wh/whRetrieve" class="btn btn-info pull-left"><i class="fa fa-reply"></i>Back</a>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
                            
       <!--New Tab Menu-->
       <hr class="separator">
        <div class="col-lg-12">
            <br>
            <div class="row">
                <ul class="nav nav-tabs">
                    <li class="${mpActive}"><a data-toggle="tab" href="#mp">Verify Barcode Label</a></li>
                    <li class="${hiActive}"><a data-toggle="tab" href="#hi">Hardware Location</a></li>
                </ul>
                <div class="tab-content">

                    <!--Tab for Scan Material Pass-->
                    <div id="mp" class="tab-pane fade ${mpActiveTab}">

                        <h6></h6>
                        <div class="col-lg-6">
                            <div class="main-box">
                                <h2>Verify Barcode Label</h2>
                                <form id="mp_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/verifyMp" method="post">
                                    <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                                    <input type="hidden" name="status" value="${whRetrieve.status}" />
                                    <input type="hidden" id="materialPassNo" name="materialPassNo" value="${whRetrieve.materialPassNo}" />
                                    <input type="hidden" name="flag" value="${whRetrieve.flag}" />
                                    <input type="hidden" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" />
                                    <input type="hidden" name="tab" value="${mpActiveTab}" />
                                    <div class="form-group">
                                        <label for=" barcodeVerify" class="col-lg-4 control-label">Scan Barcode Label</label>
                                        <div class="col-lg-8" id="barcodeVerifyDiv">
                                            <input type="text" class="form-control" id="barcodeVerify" name="barcodeVerify" autofocus="autofocus" required='true' value="${whRetrieve.barcodeVerify}"/>
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder"></div>
                                    <br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                        <button type="submit" class="btn btn-primary" name="submit" id="submit">Verify</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!--tab for inventory-->
                    <div id="hi" class="tab-pane fade ${hiActiveTab}">
                        <h6></h6>
                        <div class="col-lg-6">
                            <div class="main-box">
                                <h2>Hardware Location</h2>
                                <form id="hi_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/setInventory" method="post">
                                    <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                                    <input type="hidden" name="equipmentType" value="${whRetrieve.equipmentType}" />
                                    <input type="hidden" name="equipmentId" value="${whRetrieve.equipmentId}" />
                                    <input type="hidden" name="quantity" value="${whRetrieve.quantity}" />
                                    <input type="hidden" name="barcodeVerify" value="${whRetrieve.barcodeVerify}" />
                                    <input type="hidden" name="dateVerify" value="${whRetrieve.dateVerify}" />
                                    <input type="hidden" name="materialPassNo" value="${whRetrieve.materialPassNo}" />
                                    <input type="hidden" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" />
                                    <input type="hidden" name="status" value="${whRetrieve.status}" />
                                    <input type="hidden" name="flag" value="${whRetrieve.flag}" />
                                    <input type="hidden" name="tab" value="${hiActiveTab}" />
                                    <!--<input type="hidden" name="inventoryDate" value="${whInventory.inventoryDate}" />-->
                                    
                                    <label for=" inventoryLoc" class="col-lg-4 control-label">Hardware Location</label>
                                    <div class="col-lg-8">
                                        <input type="text" class="form-control" id="inventoryLoc" name="inventoryLoc" autofocus="autofocus" value="${whInventory.inventoryLoc}" required/> 
                                    </div>
                                    <br><br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel1">Reset</button>
                                        <button type="submit" name="submit" id="submit" class="btn btn-primary">Save</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--END tab menu-->
       
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {

                bootstrap_alert = function () {}
                bootstrap_alert.warning = function (message) {
                    $('#alert_placeholder').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                }
                if ($('#barcodeVerify').val() !== "" && $('#barcodeVerify').val() !== $('#materialPassNo').val()) {
                    bootstrap_alert.warning('Barcode Sticker NOT MATCH with Material Pass No! Please re-check and try again.');
                    //                    $("#hardwareBarcode2").effect("highlight", {}, 1000);
                    $("#hardwareBarcode2").addClass('highlight');
                }
                
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
                
                var validator = $("#mp_form").validate({
                    rules: {
                        barcodeVerify: {
                            required: true,
                            equalToBarcode: materialPassNo
                        }
                    }
                });
                var validator1 = $("#hi_form").validate({
                    rules: {
                        inventoryLoc: {
                            required: true
                        }
                    }
                });
                
                var element = $('#barcodeVerify');
                if (element.val() === "#materialPassNo") {
                    $("#submit").attr("disabled", true);
                    $("#barcodeVerify").attr("readonly", true);
                }
                
                var element = $('#inventoryLoc');
                if (element.val() !== "" || "#barcodeVerify" === "#materialPassNo") {
                    $("#submit").attr("disabled", true);
                    $("#submit1").attr("disabled", true);
                    $("#inventoryLoc").attr("readonly", true);
                }

                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".cancel1").click(function () {
                    validator1.resetForm();
                });
                
                
            });
        </script>
    </s:layout-component>
</s:layout-render>