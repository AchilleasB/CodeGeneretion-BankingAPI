import{_ as m,z as h,r as p,l as U,m as f,o as c,c as n,t as l,f as v,b as e,F as y,e as b,p as g,i as k}from"./index-mohHPoPF.js";const M={name:"UnapprovedUsers",setup(){const a=h(),o=p(""),r=p(""),t=U(()=>a.unapprovedUsers),i=async s=>{try{await a.approveUser(s),o.value="User approved successfully!",r.value="",setTimeout(()=>{o.value=""},3e3)}catch{r.value="Failed to approve user.",o.value="",setTimeout(()=>{r.value=""},3e3)}},u=async s=>{try{await a.declineUser(s),o.value="User declined successfully!",r.value="",setTimeout(()=>{o.value=""},3e3)}catch{r.value="Failed to decline user.",o.value="",setTimeout(()=>{r.value=""},3e3)}};return f(async()=>{await a.fetchUnapprovedUsers()}),{unapprovedUsers:t,successMessage:o,errorMessage:r,approveUser:i,declineUser:u}}},_=a=>(g("data-v-f57efbdc"),a=a(),k(),a),S={class:"unapproved-users"},C=_(()=>e("h2",null,"Unapproved Users",-1)),N={key:0,class:"success-message"},w={key:1,class:"error-message"},F={class:"user-table"},I=_(()=>e("thead",null,[e("tr",null,[e("th",null,"Name"),e("th",null,"Email"),e("th",null,"Actions")])],-1)),T=["onClick"],x=["onClick"];function A(a,o,r,t,i,u){return c(),n("div",S,[C,t.successMessage?(c(),n("div",N,l(t.successMessage),1)):v("",!0),t.errorMessage?(c(),n("div",w,l(t.errorMessage),1)):v("",!0),e("table",F,[I,e("tbody",null,[(c(!0),n(y,null,b(t.unapprovedUsers,s=>(c(),n("tr",{key:s.id,class:"user-item"},[e("td",null,l(s.firstName)+" "+l(s.lastName),1),e("td",null,l(s.email),1),e("td",null,[e("button",{onClick:d=>t.approveUser(s.id),class:"approve-button"},"Approve",8,T),e("button",{onClick:d=>t.declineUser(s.id),class:"decline-button"},"Decline",8,x)])]))),128))])])])}const D=m(M,[["render",A],["__scopeId","data-v-f57efbdc"]]);export{D as default};