import {useRouter} from 'next/router';
import {useEffect, useState} from 'react';

export default function TicketPage(){
  const router = useRouter();
  const {id} = router.query;
  const [ticket, setTicket] = useState(null);
  const [comments, setComments] = useState([]);
  const [text, setText] = useState('');

  useEffect(()=>{ if(!id) return; fetch(`http://localhost:8080/api/tickets/${id}`).then(r=>r.json()).then(setTicket);
    fetch(`http://localhost:8080/api/comments/ticket/${id}`).then(r=>r.json()).then(setComments);
  },[id]);

  async function addComment(e){
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user')||'null');
    if(!user){ alert('login first'); return; }
    const res = await fetch('http://localhost:8080/api/comments',{ method:'POST', headers:{'Content-Type':'application/json', 'Authorization': 'Bearer ' + localStorage.getItem('token')}, body: JSON.stringify({ ticket: { id: Number(id) }, author: { id: user.id }, text })});
    if(res.ok){ setText(''); const c = await res.json(); setComments(prev=>[...prev,c]); }
  }

  if(!ticket) return <div>Loading...</div>;
  return (
    <div>
      <h3>{ticket.subject}</h3>
      <p>{ticket.description}</p>
      <h4>Comments</h4>
      <ul>{comments.map(c=><li key={c.id}>{c.author?.email || c.author?.name}: {c.text}</li>)}</ul>
      <form onSubmit={addComment}>
        <textarea value={text} onChange={e=>setText(e.target.value)} />
        <br/>
        <button type="submit">Add Comment</button>
      </form>
    </div>
  )
}
