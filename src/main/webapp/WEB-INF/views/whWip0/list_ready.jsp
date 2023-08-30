<%-- 
    Document   : list_ready
    Created on : Aug 28, 2023, 2:47:01 PM
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
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management [Storage WIP]</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box">
                        <h2>Prepare Shipment</h2>
                        <form id="shipping_form" class="form-horizontal" role="form" action="${contextPath}/wip0hour/updateShipmentList" method="post">
                            <div class="form-group">
                                <label for="shipDate" class="col-lg-2 control-label">Shipping Date Time *</label>
                                <div class="col-lg-3">
                                    <input type="datetime-local" id="shipDate" name="shipDate" class="btn pull-right">
                                </div>
                            </div>
                            <a href="${contextPath}/wip0hour/to" class="btn btn-info pull-left" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary" style="font-family:'Orbitron', monospace;"><i class='bx bxs-printer' style='color:#ffffff' ></i> PRINT for ${wipBox}</button>
                                <input type="hidden" class="form-control" id="shippingList" name="shippingList" value="${wipBox}">
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
                            </div>
                            <div class="table-responsive">
                                <table id="dt_spml" class="table">
                                    <thead>
                                        <tr>
                                            <th><span>GTS NO</span></th>
                                            <th><span>RMS Event</span></th>
                                            <th><span>Intervals</span></th>
                                            <th><span>Quantity</span></th>
                                            <th><span>Shipment Date</span></th>
                                            <th><span>Status</span></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${wipData}" var="whWip" varStatus="whWipLoop">
                                            <tr>
                                                <td><c:out value="${whWip.gtsNo}"/></td>
                                                <td><c:out value="${whWip.rmsEvent}"/></td>
                                                <td><c:out value="${whWip.intervals}"/></td>
                                                <td><c:out value="${whWip.quantity}"/></td>
                                                <td><c:out value="${whWip.shipmentDate}"/></td>
                                                <td><c:out value="${whWip.wipStatus}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
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
                var validator = $("#shipping_form").validate({
                    rules: {
                        shipDate: {
                            required: true
                        }
                    }
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>