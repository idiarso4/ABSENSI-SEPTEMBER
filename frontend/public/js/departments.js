// Departments management wired to /api/departments
document.addEventListener('DOMContentLoaded', function() { window.departmentManager = new DepartmentManager(); });

class DepartmentManager {
  constructor(){ this.departments=[]; this.employees=[]; this.searchTerm=''; this.init(); }

  init(){ this.bind(); this.loadAll(); }

  bind(){
    const searchBtn = document.getElementById('searchBtn');
    if (searchBtn) searchBtn.addEventListener('click', ()=>{ this.searchTerm = (document.getElementById('searchInput')?.value||'').trim(); this.renderTable(); });
    const saveBtn = document.getElementById('saveDepartmentBtn');
    if (saveBtn) saveBtn.addEventListener('click', ()=> this.save());
  }

  async loadAll(){
    try{
      console.log('Loading all department data...');
      const [depRes, empRes] = await Promise.all([
        fetch('/api/v1/departments',{headers:{'Authorization':'Bearer '+this.token()}}),
        fetch('/api/v1/users',{headers:{'Authorization':'Bearer '+this.token()}})
      ]);
      console.log('API responses:', depRes.status, empRes.status);
      if (depRes.status===401 || empRes.status===401) return window.location.href='/login';
      const depData = depRes.ok ? await depRes.json() : [];
      const empData = empRes.ok ? await empRes.json() : [];
      console.log('Raw department data:', depData);
      console.log('Raw employee data:', empData);

      // Normalize payloads to arrays (supports pagination wrappers like {content:[]})
      this.departments = Array.isArray(depData) ? depData : (Array.isArray(depData?.content) ? depData.content : []);
      this.employees = Array.isArray(empData) ? empData : (Array.isArray(empData?.content) ? empData.content : []);
      console.log('Normalized departments:', this.departments);
      console.log('Normalized employees:', this.employees);

      this.updateStats();
      this.renderTable();
    }catch(e){ console.error('Load departments failed', e); this.alert('Gagal memuat data departemen','danger'); }
  }

  updateStats(){
    const totalDepts = Array.isArray(this.departments) ? this.departments.length : 0;
    const totalEmp = Array.isArray(this.employees) ? this.employees.length : 0;
    const avg = totalDepts ? Math.round(totalEmp / totalDepts) : 0;
    const td = document.getElementById('totalDepartments'); if (td) td.textContent = totalDepts;
    const te = document.getElementById('totalEmployees'); if (te) te.textContent = totalEmp;
    const av = document.getElementById('avgEmployees'); if (av) av.textContent = avg;
  }

  renderTable(){
   console.log('Rendering table with departments:', this.departments);
   const tbody = document.getElementById('departmentTableBody'); if (!tbody) return;
   const source = Array.isArray(this.departments) ? this.departments : [];
   const list = source.filter(d => !this.searchTerm || (d.name||'').toLowerCase().includes(this.searchTerm.toLowerCase()));
   console.log('Filtered list:', list);
   if (!list.length){
     console.log('No data to display');
     tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">Tidak ada data</td></tr>';
     return;
   }
    const empByDept = this.groupEmployees();
    tbody.innerHTML = list.map(d=>{
      const count = (empByDept[d.id]||[]).length;
      return `<tr>
        <td>${d.id}</td>
        <td>${d.name||'-'}</td>
        <td>${d.description||'-'}</td>
        <td>${count}</td>
        <td><span class="badge bg-primary">Aktif</span></td>
        <td>
          <button class="btn btn-sm btn-outline-primary me-1" onclick="departmentManager.edit(${d.id})"><i class="bi bi-pencil"></i></button>
          <button class="btn btn-sm btn-outline-info me-1" onclick="departmentManager.view(${d.id})"><i class="bi bi-eye"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="departmentManager.remove(${d.id})"><i class="bi bi-trash"></i></button>
        </td>
      </tr>`;
    }).join('');
  }

  groupEmployees(){
    const by = {};
    const source = Array.isArray(this.employees) ? this.employees : [];
    source.forEach(e=>{ const id = e.department?.id; if(!id) return; if(!by[id]) by[id]=[]; by[id].push(e); });
    return by;
  }

  edit(id){
    const d = this.departments.find(x=>x.id===id); if(!d) return;
    document.getElementById('departmentId').value = d.id;
    document.getElementById('departmentName').value = d.name||'';
    document.getElementById('departmentDescription').value = d.description||'';
    document.getElementById('departmentModalLabel').textContent='Edit Departemen';
    document.getElementById('saveDepartmentBtn').textContent='Perbarui Departemen';
    new bootstrap.Modal(document.getElementById('departmentModal')).show();
  }

  view(id){
    const d = this.departments.find(x=>x.id===id); if(!d) return;
    document.getElementById('detailsName').textContent = d.name||'-';
    document.getElementById('detailsDescription').textContent = d.description||'-';
    const list = document.getElementById('employeeList'); list.innerHTML='';
    const emp = this.groupEmployees()[id]||[];
    document.getElementById('detailsEmployeeCount').textContent = emp.length;
    document.getElementById('detailsStatus').textContent = 'Aktif';
    document.getElementById('detailsStatus').className = 'badge bg-success';
    if(!emp.length){ list.innerHTML='<li class="list-group-item text-center text-muted">Belum ada karyawan</li>'; }
    else emp.forEach(e=>{ const li=document.createElement('li'); li.className='list-group-item'; li.textContent=`${e.firstName||''} ${e.lastName||''}`.trim(); list.appendChild(li); });
    new bootstrap.Modal(document.getElementById('departmentDetailsModal')).show();
  }

  async save(){
    const id = document.getElementById('departmentId').value;
    const payload = { name: (document.getElementById('departmentName').value||'').trim(), description: document.getElementById('departmentDescription').value||null };
    console.log('Saving department:', payload);

    if(!payload.name) return this.alert('Nama departemen wajib diisi','warning');

    try{
      const res = await fetch(id?`/api/v1/departments/${id}`:'/api/v1/departments',{ method: id?'PUT':'POST', headers:{'Content-Type':'application/json','Authorization':'Bearer '+this.token()}, body: JSON.stringify(payload)});
      console.log('Response status:', res.status, 'ok:', res.ok);

      if(!res.ok){
        let msg = 'Gagal menyimpan departemen';
        try{ const err = await res.json(); msg = err.message || err.error || msg; }catch(_e){}
        console.error('Error response:', msg);
        return this.alert(msg,'danger');
      }

      // Handle successful response - backend might return Department object or empty response
      let responseData;
      try{
        responseData = await res.json();
        console.log('Success response data:', responseData);
      }catch(e){
        // If response is empty or not JSON, that's OK for successful creation
        console.log('Response parsing failed, treating as success');
        responseData = null;
      }

      console.log('Closing modal and reloading data');
      const modal = bootstrap.Modal.getInstance(document.getElementById('departmentModal'));
      if(modal) modal.hide();

      document.getElementById('departmentForm').reset();
      await this.loadAll();
      this.alert('Departemen disimpan berhasil','success');
      console.log('Department saved successfully');

      // Trigger global department refresh for other pages
      window.dispatchEvent(new CustomEvent('departmentsUpdated'));
    }catch(e){
      console.error('Save department error:', e);
      this.alert('Error menyimpan departemen: ' + e.message,'danger');
    }
  }

  async remove(id){ if(!confirm('Hapus departemen ini?')) return; try{ const res=await fetch(`/api/v1/departments/${id}`,{method:'DELETE',headers:{'Authorization':'Bearer '+this.token()}}); if(res.ok){ this.alert('Departemen dihapus','success'); this.loadAll(); } else this.alert('Gagal menghapus','danger'); }catch(e){ this.alert('Error menghapus','danger'); } }

  token(){ return localStorage.getItem('token')||''; }
  alert(msg,type='info'){ const d=document.createElement('div'); d.className=`alert alert-${type} alert-dismissible fade show position-fixed`; d.style.top='20px'; d.style.right='20px'; d.style.zIndex='9999'; d.innerHTML=`${msg}<button type="button" class="btn-close" data-bs-dismiss="alert"></button>`; document.body.appendChild(d); setTimeout(()=>{d.remove();},4000); }
}
