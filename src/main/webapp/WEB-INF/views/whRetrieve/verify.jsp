<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            .fa-stack {
                color: red;
            }
            .table-link {
                color: red;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">

        <div class="col-lg-12">
            <h1>Approval for Hardware Retrieve</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="approval_hardwaretrieve_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/approvalupdate" method="post">
                            <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-3 control-label">Hardware Type</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whRetrieve.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-3 control-label">Hardware ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whRetrieve.equipmentId}" readonly>
                                </div>
                            </div>  
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-3 control-label">Quantity *</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whRetrieve.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-3 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whRetrieve.materialPassNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-3 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveBy" class="col-lg-3 control-label">Requested By</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveBy" name="retrieveBy" placeholder="Retrieve For" value="${whRetrieve.requestedBy}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveDate" class="col-lg-3 control-label">Requested Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveDate" name="retrieveDate" placeholder="Retrieve For" value="${whRetrieve.requestedDate}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="remarks" class="col-lg-3 control-label">Remarks </label>
                                <div class="col-lg-8">
                                    <textarea class="form-control" rows="5" id="remarks" name="remarks" readonly>${whRetrieve.remarks}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-lg-3 control-label">Status</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="status" name="status" value="${whRetrieve.status}" readonly>
                                </div>
                            </div>
                            <a href="${contextPath}/wh/whRetrieve" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
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
                        <div class="col-lg-8">
                            <div class="main-box">
                                <h2>Verify Barcode Label</h2>
                                <form id="mp_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/verifyMp" method="post">
                                    <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                                    <input type="hidden" name="status" value="${whRetrieve.status}" />
                                    <input type="hidden" id="materialPassNo" name="materialPassNo" value="${whRetrieve.materialPassNo}" />
                                    <input type="hidden" id="flag" name="flag" value="${whRetrieve.flag}" />
                                    <input type="hidden" id="requestedEmail" name="requestedEmail" value="${whRetrieve.requestedEmail}" />
                                    <input type="hidden" id="requestedBy" name="requestedBy" value="${whRetrieve.requestedBy}" />
                                    <input type="hidden" id="equipmentId" name="equipmentId" value="${whRetrieve.equipmentId}" />
                                    <input type="hidden" id="equipmentType" name="equipmentType" value="${whRetrieve.equipmentType}" />
                                    <input type="hidden" id="materialPassExpiry" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" />
                                    <input type="hidden" name="tempCount" id="tempCount" value="${whRetrieve.tempCount}" />
                                    <input type="hidden" name="tab" value="${mpActiveTab}" />
                                    <div class="form-group">
                                        <label for=" barcodeVerify" class="col-lg-3 control-label">Scan Barcode Label</label>
                                        <div class="col-lg-8" id="barcodeVerifyDiv">
                                            <input type="text" class="form-control" id="barcodeVerify" name="barcodeVerify" autofocus="autofocus" value="${whRetrieve.barcodeVerify}" />
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder"></div>
                                    <br><br>
                                    <div class="pull-left" id="emaildiv" hidden>
                                        <!--<a href="${contextPath}/wh/whRetrieve/error/${whRetrieve.refId}" id="error" name="error" class="table-link danger group_delete" onclick="modalEmail(this);">Send Email</button></a>-->
                                        <a href="${contextPath}/wh/whRetrieve/error/${whRetrieve.refId}" id="error" name="error" class="table-link danger group_delete" >Send Email
                                            <span class="fa-stack" >
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-envelope fa-stack-1x fa-inverse"></i>
                                            </span>
                                        </a>
                                    </div>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel" id="reset1">Reset</button>
                                        <button type="submit" class="btn btn-primary" name="submit" id="submit1">Verify</button>

                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!--tab for inventory-->
                    <div id="hi" class="tab-pane fade ${hiActiveTab}">
                        <h6></h6>
                        <div class="col-lg-8">
                            <div class="main-box">
                                <h2>Hardware Location</h2>
                                <form id="hi_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/setInventory" method="post">
                                    <input type="hidden" name="refId" value="${whRetrieve.refId}" />
                                    <input type="hidden" id="equipmentType2" name="equipmentType" value="${whRetrieve.equipmentType}" />
                                    <input type="hidden" id="equipmentId" name="equipmentId" value="${whRetrieve.equipmentId}" />
                                    <input type="hidden" id="quantity" name="quantity" value="${whRetrieve.quantity}" />
                                    <input type="hidden" id="barcodeVerify" name="barcodeVerify" value="${whRetrieve.barcodeVerify}" />
                                    <input type="hidden" id="dateVerify" name="dateVerify" value="${whRetrieve.dateVerify}" />
                                    <input type="hidden" id="materialPassNo" name="materialPassNo" value="${whRetrieve.materialPassNo}" />
                                    <input type="hidden" id="materialPassExpiry" name="materialPassExpiry" value="${whRetrieve.materialPassExpiry}" />
                                    <input type="hidden" id="status" name="status" value="${whRetrieve.status}" />
                                    <input type="hidden" id="flag" name="flag" value="${whRetrieve.flag}" />
                                    <input type="hidden" id="tab" name="tab" value="${hiActiveTab}" />
                                    <!--<input type="hidden" name="inventoryDate" value="${whInventory.inventoryDate}" />-->
                                    <div class="form-group">
                                        <label for="tempRack" class="col-lg-3 control-label">Rack Location</label>
                                        <div class="col-lg-8">
                                            <input type="text" class="form-control" id="tempRack" name="tempRack" autofocus="autofocus" value="${whRetrieve.tempRack}" /> 
                                        </div>
                                        <br><br>
                                        <label for="tempShelf" class="col-lg-3 control-label">Shelf Location</label>
                                        <div class="col-lg-8">
                                            <input type="text" class="form-control" id="tempShelf" name="tempShelf" autofocus="autofocus" value="${whRetrieve.tempShelf}" /> 
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder2"></div>
                                    <c:if test="${checkValidity == 'false'}">
                                        <div id = "alert_placeholder3"></div>
                                    </c:if>
                                    <br><br>
                                    <div class="pull-right">
                                        <button type="reset" id="reset2" class="btn btn-secondary cancel1">Reset</button>
                                        <button type="submit" name="submit" id="submit2" class="btn btn-primary">Save</button>
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
                //temporary disabled.
                $('#barcodeVerify').bind('copy paste cut', function (e)  {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                $('#tempRack').bind('copy paste cut', function (e)  {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                $('#tempShelf').bind('copy paste cut', function (e)  {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                var validator = $("#mp_form").validate({
                    rules: {
                        barcodeVerify: {
                            required: true
                        }
                    }
                });
                $(".cancel").click(function () {
                    validator.resetForm();
                });

                var validator1 = $("#hi_form").validate({
                    rules: {
                        tempRack: {
                            required: true,
                            minlength: 6,
                            maxlength: 6
                        },
                        tempShelf: {
                            required: true,
                            minlength: 10,
                            maxlength: 10                            
                        }
                    }
                });

                $(".cancel1").click(function () {
                    validator1.resetForm();
                });

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
                
                bootstrap_alert3 = function () {};
                bootstrap_alert3.warning = function (message) {
                    $('#alert_placeholder3').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                };
                bootstrap_alert3.warning('Inventory invalid! Please re-check and try again.');

                bootstrap_alert2 = function () {};
                bootstrap_alert2.warning = function (message) {
                    $('#alert_placeholder').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                };
                if ($('#barcodeVerify').val() !== "" && $('#barcodeVerify').val() !== $('#materialPassNo').val()) {
                    bootstrap_alert2.warning('Barcode Sticker NOT MATCH with Material Pass No! Please re-check and try again.');
                    $("#hardwareBarcode2").addClass('highlight');
                }
                
                bootstrap_alert = function () {};
                bootstrap_alert.warning = function (message) {
                    $('#alert_placeholder2').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                };
                if ($('#tempRack').val() !== $('#tempShelf').val().substring(0,6) || $('#tempRack').val().length !== 6 && $('#tempShelf').val().length !== 10 
                    && ($('#tempRack').val() !== "" && $('#tempShelf').val() !== "")) {
                    bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                    $("#tempRack").addClass('highlight');
                    $("#tempShelf").addClass('highlight');
                } else {
                    if ($('#equipmentType2').val() === 'Motherboard' && ($('#tempRack').val() !== "" && $('#tempShelf').val() !== "")) {
                        if($('#tempRack').val().substring(0,4) === "S-SY" || $('#tempRack').val().substring(0,4) === "S-AC" || $('#tempRack').val().substring(0,4) === "S-WF" || $('#tempRack').val().substring(0,4) === "S-SY" || $('#tempRack').val().substring(0,4) === "S-IO" || $('#tempRack').val().substring(0,4) === "S-BB" || $('#tempRack').val().substring(0,4) === "S-HA" || $('#tempRack').val().substring(0,4) === "S-PT") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#tempRack").addClass('highlight');
                            $("#tempShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType2').val() === 'Stencil' && ($('#tempRack').val() !== "" && $('#tempShelf').val() !== "")) {
                        if($('#tempRack').val().substring(0,4) === "S-ST") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#tempRack").addClass('highlight');
                            $("#tempShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType2').val() === 'Tray' && ($('#tempRack').val() !== "" && $('#tempShelf').val() !== "")) {
                        if($('#tempRack').val().substring(0,4) === "S-TJ" || $('#tempRack').val().substring(0,4) === "S-TR") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#tempRack").addClass('highlight');
                            $("#tempShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType2').val() === 'PCB' && ($('#tempRack').val() !== "" && $('#tempShelf').val() !== "")) {
                        if($('#tempRack').val().substring(0,4) === "S-PC") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#tempRack").addClass('highlight');
                            $("#tempShelf").addClass('highlight');
                        }
                    }
                }

                var element2 = $('#tempCount');
                if (element2.val() === "0" || element2.val() === "1" || element2.val() === "2") {
                    $("#emaildiv").hide();
                } else {
                    $("#emaildiv").show();
                }
                
                var element1 = $('#barcodeVerify');
                var element11 = $('#materialPassNo');
                if (element1.val() === element11.val()) {
                    $("#submit1").attr("disabled", true);
                    $("#reset1").attr("disabled", true);
                    $("#barcodeVerify").attr("readonly", true);
                    $("#emaildiv").hide();
                } else {
                    $("#submit2").attr("disabled", true);
                    $("#reset2").attr("disabled", true);
                    $("#tempRack").attr("readonly", true);
                    $("#tempShelf").attr("readonly", true);
                }
                
                var element2 = $('#status');
                if (element2.val() === 'Move to Inventory') {
                    $("#submit2").attr("disabled", true);
                    $("#reset2").attr("disabled", true);
                    $("#tempRack").attr("readonly", true);
                    $("#tempShelf").attr("readonly", true);
                }
                
            });
        </script>
    </s:layout-component>
</s:layout-render>