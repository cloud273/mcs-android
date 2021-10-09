APPCENTER

1. PATIENT APP: https://bit.ly/3ciabwd

2. DOCTOR APP: https://bit.ly/39ejKdU


DOCTOR APP BUG REPORT

0. Update app name + localization app name + icon app
1. Xài color va font size define trong resource define trong mcs/res/values/color.xml, dimens.xml
2. Trong tab lịch khám
    + segment hơi nhỏ khi chạy trên tablet
    + chon all, sau do vao detail một cuộc khám, quay lại thì vào lại tab Today
    + khi mới login vô thì mất line devider giữa cell 1 và 2 
3. Trong detail Appointment,
    + dưới hình có 1 devider -> xoá
    + muc trạng thái, so thứ tự, tên bệnh nhân, chuyên khoa, thời gian: highlight nội dung chứ ko phải title -> đã có cell làm sẵn, e đổi cellID là okie - vào trong mcs phần model/mcsrecylerRender sẽ thấy danh sách cell hỗ trợ a làm sẵn rôì
    + nút begin. reject, finish theo màu define trong xml của app
4. Trong history appointment
    + background màu trắng luôn để trùng với cell, khi không có cuộc hẹn thì sẽ thấy rõ
    + order  text trong cell chua dc localized
5. Trong tab Setting
    + background màu trắng luôn để trùng với cell (bên app patient a cũng sẽ sừa lại luôn chứ nhìn nó kỳ quá, hiii)
    + bấm vào Abous Us, FAQ, Term condition va contact chưa mở web lên
6. Giao diện doctor detail
    + Mấy cell transparent trong khi background ko phải màu trắng
    + Mấy cell ko highlight title giong IOS, đổi cell lại là okie 
    + title Basic_information chua dc localized
7. Giao dien update password
    + Title va button chua dc localized
    + Nut hien thi mat khau chua dc localized
8. Giao dien Cinic detail
    + TItle cell certificate chua dc highlight
    + work_phone chua dc localize (bug ca IOS, nen a se them vao cho ca 2)

9. Giao dien Chi tiet goi kham benh
    + Title chua dc highlight
10. Schedule
    + chinh Background mau trang het
