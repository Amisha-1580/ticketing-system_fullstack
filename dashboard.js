import {useEffect, useState} from 'react';
import Link from 'next/link';

export default function Dashboard(){
  const [tickets, setTickets] = useState([]);
  useEffect(()=>{ fetch('http://localhost:8080/api/tickets', { headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') } }).then(r=>r.json()).then(setTickets); },[]);
  return (
    <div>
      <h2>Dashboard</h2>
      <p><Link href="/">Home</Link> | <Link href="/ticket/new">New Ticket</Link></p>
      <ul>
        {tickets.map(t=>(
          <li key={t.id}>
            <strong>{t.subject}</strong> — {t.status} — owner: {t.owner? t.owner.email : 'n/a'}
            <div><Link href={`/ticket/${t.id}`}>Open</Link></div>
          </li>
        ))}
      </ul>
    </div>
  )
}
