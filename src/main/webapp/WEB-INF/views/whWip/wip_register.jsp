<%-- 
    Document   : register_page
    Created on : Jun 23, 2023, 10:16:03 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/css/005.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
                table thead {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
                table tbody {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
            }
            .dataTables_wrapper .dt-buttons {
                float:none;  
                text-align:right;
            }
        </style>
    </s:layout-component>
        <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management [Stress WIP]</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan Trip Ticket</h2>
                        <form id="register_wip_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateVerifyToRegister" method="post">
                            <div class="form-group">
                                <label for="tripTicket" class="col-lg-3 control-label">Trip Ticket *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="tripTicket" name="tripTicket" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="intervals" class="col-lg-3 control-label">Intervals *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="intervals" name="intervals" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="quantity" class="col-lg-3 control-label">Quantity *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                                <a href="${contextPath}/whWip/to" class="btn btn-info pull-left" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel" style="font-family:'Orbitron', monospace;"><i class='bx bx-reset bx-fw' ></i> Reset</button>
                                <button type="submit" class="btn btn-primary" style="font-family:'Orbitron', monospace;"><i class='bx bx-registered bx-fw' style='color:#ffffff' ></i> Register</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <div class="col-lg-12">
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">WIP [Stress] Information - Ready For Shipment</h2>
                            <a href="${contextPath}/whWip/listReady" class="btn btn-primary pull-right" style="font-family:'Orbitron', monospace;">
                                <i class='bx bxs-truck bx-fw'></i> Prepare Shipment
                            </a>
                        </div>
                        <hr/>
                        <div class="clearfix">
                            <div class="form-group pull-left">
                                <select id="dt_spml_rows" class="form-control">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;">
                                </div>
                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <input type="hidden" class="form-control" id="count" name="count" value="${count}">
                            <input type="hidden" class="form-control" id="countAll" name="countAll" value="${countAll}">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>GTS Number</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Remove</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${packingList}" var="packingList" varStatus="packingListLoop">
                                        <tr>
                                            <td><c:out value="${packingListLoop.index+1}"/></td>
                                            <td><c:out value="${packingList.gtsNo}"/></td>
                                            <td><c:out value="${packingList.rmsEvent}"/></td>
                                            <td><c:out value="${packingList.intervals}"/></td>
                                            <td><c:out value="${packingList.shipQuantity} / ${packingList.quantity}"/></td>
                                            <td>
                                                <a href="${contextPath}/whWip/updateRegisterToVerify/${packingList.requestId}" class="table-link" title="Remove">
                                                    <i class='bx bx-message-alt-x bx-rotate-90 bx-md bx-fw' style='color:#ff0000'></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--print-->
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
        <script src="${contextPath}/resources/validation\jquery.validate.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                
                if ($('#countAll').val() === '0') {
                    $('#print').hide();
                } else {
                    $('#print').show();
                }
                
                if ($('#countAll').val() === '0') {
                    $('#deleteAll').hide();
                } else {
                    $('#deleteAll').show();
                }
                
                var validator = $("#register_wip_form").validate({
                    rules: {
                        tripTicket: {
                            required: true
                        },
                        intervals: {
                            number: true,
                            required: true
                        },
                        quantity: {
                            number: true
                        }
                    }
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $('#tripTicket').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>