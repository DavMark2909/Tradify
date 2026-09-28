import './StatusMessage.css';

export default function StatusMessage({ tone = 'empty', children }) {
  return <p className={`status-message status-message--${tone}`}>{children}</p>;
}
