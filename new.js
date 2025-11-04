import {useState} from 'react';
import {useRouter} from 'next/router';

export default function NewTicket(){
  const [subject,setSubject]=useState('');
  const [desc,setDesc]=useState('');
  const [priority,setPriority]=useState('MEDIUM');
  const router = useRouter();

  async function submit(e){
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user')||'null');
    if(!user){ alert('login'); return; }
    const res = await fetch('http://localhost:8080/api/tickets', {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ subject, description: desc, priority, owner: { id: user.id } })
    });
    if(res.ok){ router.push('/dashboard'); } else { alert('error'); }
  }

  return (
    <div>
      <h2>New Ticket</h2>
      <form onSubmit={submit}>
        <div><input placeholder="Subject" value={subject} onChange={e=>setSubject(e.target.value)} /></div>
        <div><textarea placeholder="Description" value={desc} onChange={e=>setDesc(e.target.value)} /></div>
        <div>
          <select value={priority} onChange={e=>setPriority(e.target.value)}>
            <option>LOW</option><option>MEDIUM</option><option>HIGH</option><option>URGENT</option>
          </select>
        </div>
        <button type="submit">Create</button>
      </form>
    </div>
  )
}
