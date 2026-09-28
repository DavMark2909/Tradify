import './AppShell.css';

export default function AppShell({ header, sidebar, main }) {
  return (
    <div className="app-shell">
      {header}
      {sidebar}
      {main}
    </div>
  );
}
